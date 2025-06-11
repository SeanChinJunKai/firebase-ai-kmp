@file:OptIn(ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import cocoapods.FirebaseAIBridge.*
import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerativeBackend
import io.github.seanchinjunkai.firebase.ai.type.UnknownException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


public actual object Firebase {
    public actual fun ai(backend: GenerativeBackend): FirebaseAI {
        return when (backend) {
            GenerativeBackend.GOOGLE_AI -> FirebaseAI(FirebaseObjc.aiWithBackend(GenerativeBackendObjcGoogleAI))
            GenerativeBackend.VERTEX_AI -> FirebaseAI(FirebaseObjc.aiWithBackend(GenerativeBackendObjcVertexAI))
        }
    }
}


actual class FirebaseAI internal constructor(val iOSFirebaseAI: FirebaseAIObjc) {
    public actual fun generativeModel(modelName: String): GenerativeModel {
        return GenerativeModel(iOSFirebaseAI.generativeModelWithModelName(modelName))
    }
}


actual class GenerativeModel internal constructor(val iOSGenerativeModel: GenerativeModelObjc) {
    public actual suspend fun generateContent(prompt: String): GenerateContentResponse =
        suspendCancellableCoroutine { continuation ->
            iOSGenerativeModel.generateContentWithPrompt(
                prompt,
                completionHandler = { result: GenerateContentResponseObjc?, error: NSError? ->
                    val result = result?.toGenerateContentResponse()
                    when {
                        error != null -> continuation.resumeWithException(error.toFirebaseAIException())
                        result != null -> continuation.resume(result)
                        else -> continuation.resumeWithException(UnknownException("No generateContent result and no error returned."))
                    }
                })
        }

    public actual fun generateContentStream(prompt: String): Flow<GenerateContentResponse> =
        callbackFlow {
            iOSGenerativeModel.generateContentStreamWithPrompt(
                prompt,
                onResponse = {
                    val response = it?.toGenerateContentResponse()
                    response?.let { element ->
                        trySendBlocking(element)
                    }
                },
                onComplete = { error ->
                    if (error != null) {
                        close(error.toFirebaseAIException())
                    } else {
                        close()
                    }
                }
            )
            awaitClose {

            }
        }



    public actual suspend fun generateContent(vararg prompt: Content): GenerateContentResponse =
        suspendCancellableCoroutine { continuation ->
            val contents = prompt.map { it.toiOSContent() }
            iOSGenerativeModel.generateContentWithContent(
                contents,
                completionHandler = { result: GenerateContentResponseObjc?, error: NSError? ->
                    val result = result?.toGenerateContentResponse()
                    when {
                        error != null -> continuation.resumeWithException(error.toFirebaseAIException())
                        result != null -> continuation.resume(result)
                        else -> continuation.resumeWithException(UnknownException("No generateContent result and no error returned."))
                    }
                })
        }

    public actual fun generateContentStream(vararg prompt: Content): Flow<GenerateContentResponse> =
        callbackFlow {
            val contents = prompt.map { it.toiOSContent() }
            iOSGenerativeModel.generateContentStreamWithContent(
                contents,
                onResponse = {
                    val response = it?.toGenerateContentResponse()
                    response?.let { element ->
                        trySendBlocking(element)
                    }
                },
                onComplete = { error ->
                    if (error != null) {
                        close(error.toFirebaseAIException())
                    } else {
                        close()
                    }
                }
            )
            awaitClose {

            }
        }

    public actual suspend fun countTokens(prompt: String): CountTokensResponse =
        suspendCancellableCoroutine { continuation ->
            iOSGenerativeModel.countTokensWithPrompt(
                prompt,
                completionHandler = { result: CountTokensResponseObjc?, error: NSError? ->
                    when {
                        error != null -> continuation.resumeWithException(error.toFirebaseAIException())
                        result != null -> continuation.resume(result.toCountTokensResponse())
                        else -> continuation.resumeWithException(UnknownException("No countTokens result and no error returned."))
                    }
                })
        }

    public actual suspend fun countTokens(vararg prompt: Content): CountTokensResponse =
        suspendCancellableCoroutine { continuation ->
            val contents = prompt.map { it.toiOSContent() }
            iOSGenerativeModel.countTokensWithContent(
                contents,
                completionHandler = { result: CountTokensResponseObjc?, error: NSError? ->
                    when {
                        error != null -> continuation.resumeWithException(error.toFirebaseAIException())
                        result != null -> continuation.resume(result.toCountTokensResponse())
                        else -> continuation.resumeWithException(UnknownException("No countTokens result and no error returned."))
                    }
                })
        }
}