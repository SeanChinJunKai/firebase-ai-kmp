package io.github.seanchinjunkai.firebase.ai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.FileDataPart
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.ImagePart
import com.google.firebase.ai.type.InlineDataPart
import com.google.firebase.ai.type.Part
import com.google.firebase.ai.type.TextPart
import com.google.firebase.ai.type.content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import com.google.firebase.ai.GenerativeModel as AndroidGenerativeModel
import com.google.firebase.ai.FirebaseAI as AndroidFirebaseAI


public actual object Firebase {
    // TODO: App parameter currently missing
    public actual fun ai(backend: GenerativeBackendEnum): FirebaseAI {
        return when (backend)  {
            GenerativeBackendEnum.GOOGLE_AI -> FirebaseAI(Firebase.ai(backend = GenerativeBackend.googleAI()))
            GenerativeBackendEnum.VERTEX_AI -> FirebaseAI(Firebase.ai(backend = GenerativeBackend.vertexAI()))
        }
    }
}



actual class FirebaseAI internal constructor(internal val androidFirebaseAI: AndroidFirebaseAI) {
    // TODO: Add missing parameters
    public actual fun generativeModel(modelName: String): GenerativeModel {
        return GenerativeModel(androidFirebaseAI.generativeModel(modelName))
    }
}


actual class GenerativeModel internal constructor(internal val androidGenerativeModel: AndroidGenerativeModel) {
    public actual suspend fun generateContent(prompt: String): String {
        return androidGenerativeModel.generateContent(prompt).text ?: "No content found"
    }

    public actual suspend fun generateContent(vararg prompt: PromptPart): String {
        val content = content {
            prompt.map {
                part(it.toPart())
            }
        }
        return androidGenerativeModel.generateContent(content).text ?: "No content found"
    }

    public actual suspend fun countTokens(prompt: String): CountTokensResponse {
        return androidGenerativeModel.countTokens(prompt).toCountTokensResponse()
    }

    public actual suspend fun countTokens(vararg prompt: PromptPart): CountTokensResponse {
        val content = content {
            prompt.map {
                part(it.toPart())
            }
        }
        return androidGenerativeModel.countTokens(content).toCountTokensResponse()
    }
}

public fun PromptPart.toPart(): Part {
    return when (this) {
        is PromptPart.TextPart -> TextPart(this.text)
        is PromptPart.FileDataPart -> FileDataPart(this.uri, this.mimeType)
        is PromptPart.InlineDataPart -> InlineDataPart(this.inlineData, this.mimeType)
        is PromptPart.ImagePart -> ImagePart(this.image)
        else -> throw error("Unknown prompt part type")
    }
}
