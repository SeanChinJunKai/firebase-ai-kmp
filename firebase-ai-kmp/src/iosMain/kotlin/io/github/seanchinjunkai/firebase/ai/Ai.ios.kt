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
import io.github.seanchinjunkai.firebase.ai.type.UnknownException
import io.github.seanchinjunkai.firebase.ai.type.content
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch


public actual object Firebase {
    public actual fun ai(backend: GenerativeBackend): FirebaseAI {
        return when (backend.backend) {
            GenerativeBackendEnum.GOOGLE_AI -> FirebaseAI(FirebaseObjc.aiWithBackend(GenerativeBackendObjc.googleAI()))
            GenerativeBackendEnum.VERTEX_AI -> FirebaseAI(FirebaseObjc.aiWithBackend(
                GenerativeBackendObjc.vertexAIWithLocation(backend.location)))
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
        generateContent(content { text(prompt) })

    public actual fun generateContentStream(prompt: String): Flow<GenerateContentResponse> =
        generateContentStream(content { text(prompt) })



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
        channelFlow {
            val contents = prompt.map { it.toiOSContent() }
            val jobs = mutableListOf<Job>()
            iOSGenerativeModel.generateContentStreamWithContent(
                contents,
                onResponse = {
                    val response = it?.toGenerateContentResponse()
                    response?.let { element ->
                        val job = launch {
                            send(response)
                        }
                        jobs.add(job)
                    }
                },
                onComplete = { error ->
                    launch {
                        joinAll(*jobs.toTypedArray())
                        if (error != null) {
                            close(error.toFirebaseAIException())
                        } else {
                            close()
                        }
                    }
                }
            )
        }
    public actual suspend fun countTokens(prompt: String): CountTokensResponse =
        countTokens(content { text(prompt) })

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

    public actual fun startChat(history: List<Content>): Chat =
        Chat(this, history.toMutableList())
}