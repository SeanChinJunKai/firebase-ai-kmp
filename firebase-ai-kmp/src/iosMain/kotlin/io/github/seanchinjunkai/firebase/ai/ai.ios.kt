@file:OptIn(ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import cocoapods.FirebaseAIBridge.*


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
                completion = { result: String?, error: NSError? ->
                    val string: String = result ?: "No result"
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
    public actual suspend fun generateContentV2(prompt: String): String =
        suspendCancellableCoroutine { continuation ->
            iOSGenerativeModel.generateContentV2WithPrompt(
                prompt,
                completionHandler = { result: String?, error: NSError? ->
                    val string: String = result ?: "No result"
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
                completion = { result: String?, error: NSError? ->
                    val string: String = result ?: "No result"
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
}


public fun PromptPart.toPart(): PartObjc {
    return when (this) {
        is PromptPart.TextPart -> TextPartObjc(this.text)
        is PromptPart.FileDataPart -> FileDataPartObjc(this.uri, this.mimeType)
        is PromptPart.InlineDataPart -> InlineDataPartObjc(this.inlineData.toNSData(), this.mimeType)
        is PromptPart.ImagePart -> ImagePartObjc(this.image)
        else -> throw error("Unknown prompt part type")
    }
}
