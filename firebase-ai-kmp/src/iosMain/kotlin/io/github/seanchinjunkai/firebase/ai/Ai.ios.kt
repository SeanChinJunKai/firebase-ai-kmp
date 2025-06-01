@file:OptIn(ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import cocoapods.FirebaseAIBridge.*
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse


public actual object Firebase {
    public actual fun ai(backend: GenerativeBackendEnum): FirebaseAI {
        return FirebaseAI(FirebaseObjc.aiWithBackend(NSNumber(backend.ordinal)))
    }
}


actual class FirebaseAI internal constructor(val iOSFirebaseAI: FirebaseAIObjc) {
    public actual fun generativeModel(modelName: String): GenerativeModel {
        return GenerativeModel(iOSFirebaseAI.generativeModelWithModelName(modelName))
    }
}


actual class GenerativeModel internal constructor(val iOSGenerativeModel: GenerativeModelObjc) {
    public actual suspend fun generateContent(prompt: String): String =
        suspendCancellableCoroutine { continuation ->
            iOSGenerativeModel.generateContentWithPrompt(
                prompt,
                completionHandler = { result: GenerateContentResponseObjc?, error: NSError? ->
                    val string: String = result?.text() ?: "No result"
                    when {
                        error != null -> continuation.resumeWithException(
                            Exception(
                                error.localizedDescription
                            )
                        )

                        result != null -> continuation.resume(string)
                        else -> continuation.resumeWithException(Exception("No result and no error returned."))
                    }
                })
        }
    public actual suspend fun generateContent(vararg prompt: PromptPart): String =
        suspendCancellableCoroutine { continuation ->
            val parts: List<PartObjc> = prompt.map { it.toPart() }
            iOSGenerativeModel.generateContentWithParts(
                parts,
                completionHandler = { result: GenerateContentResponseObjc?, error: NSError? ->
                    val string: String = result?.text() ?: "No result"
                    when {
                        error != null -> continuation.resumeWithException(
                            Exception(
                                error.localizedDescription
                            )
                        )
                        result != null -> continuation.resume(string)
                        else -> continuation.resumeWithException(Exception("No result and no error returned."))
                    }
                })
        }

    public actual suspend fun countTokens(prompt: String): CountTokensResponse =
        suspendCancellableCoroutine { continuation ->
            iOSGenerativeModel.countTokensWithPrompt(
                prompt,
                completionHandler = { result: CountTokensResponseObjc?, error: NSError? ->
                    when {
                        error != null -> continuation.resumeWithException(
                            Exception(
                                error.localizedDescription
                            )
                        )

                        result != null -> continuation.resume(result.toCountTokensResponse())
                        else -> continuation.resumeWithException(Exception("No result and no error returned."))
                    }
                })
        }

    public actual suspend fun countTokens(vararg prompt: PromptPart): CountTokensResponse =
        suspendCancellableCoroutine { continuation ->
            val parts: List<PartObjc> = prompt.map { it.toPart() }
            iOSGenerativeModel.countTokensWithParts(
                parts,
                completionHandler = { result: CountTokensResponseObjc?, error: NSError? ->
                    when {
                        error != null -> continuation.resumeWithException(
                            Exception(
                                error.localizedDescription
                            )
                        )

                        result != null -> continuation.resume(result.toCountTokensResponse())
                        else -> continuation.resumeWithException(Exception("No result and no error returned."))
                    }
                })
        }

}


public fun PromptPart.toPart(): PartObjc {
    return when (this) {
        is PromptPart.TextPart -> TextPartObjc(this.text)
        is PromptPart.FileDataPart -> FileDataPartObjc(this.uri, this.mimeType)
        is PromptPart.InlineDataPart -> InlineDataPartObjc(this.inlineData.toNSData(), this.mimeType)
        is PromptPart.ImagePart -> ImagePartObjc(this.image)
    }
}
