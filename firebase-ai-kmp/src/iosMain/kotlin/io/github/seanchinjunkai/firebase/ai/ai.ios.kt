@file:OptIn(ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import cocoapods.FirebaseAIBridge.*
import io.github.seanchinjunkai.firebase.ai.type.Content


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
    public actual suspend fun generateContent(vararg prompt: Content): String =
        suspendCancellableCoroutine { continuation ->
            val input = prompt.map { it.toiOSContent() }.toTypedArray()
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

public fun Content.toiOSContent(): ModelContentObjc {
    return ModelContentObjc(
        this.role,
        this.parts.map { it.toiOSPart() }
    )
}

public fun Part.toiOSPart(): PartObjc {
    return when (this) {
        is TextPart -> TextPartObjc(this.text)
        is FileDataPart -> FileDataPartObjc(this.uri, this.mimeType)
        is InlineDataPart -> InlineDataPartObjc(this.inlineData.toNSData(), this.mimeType)
        is ImagePart -> ImagePartObjc(this.image)
        else -> throw error("Unknown prompt part type")
    }
}
