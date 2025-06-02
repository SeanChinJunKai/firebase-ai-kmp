package io.github.seanchinjunkai.firebase.ai

import android.graphics.Bitmap
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content as AndroidContent
import com.google.firebase.ai.type.FileDataPart as AndroidFileDataPart
import com.google.firebase.ai.type.GenerativeBackend
import io.github.seanchinjunkai.firebase.ai.type.Content
import com.google.firebase.ai.type.ImagePart as AndroidImagePart
import com.google.firebase.ai.type.InlineDataPart as AndroidInlineDataPart
import com.google.firebase.ai.type.Part as AndroidPart
import com.google.firebase.ai.type.TextPart as AndroidTextPart
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

    public actual suspend fun generateContent(vararg prompt: Content): String {
        val input = prompt.map { it.toAndroidContent() }.toTypedArray()
        return androidGenerativeModel.generateContent(*input).text ?: "No content found"
    }
}

public fun Content.toAndroidContent(): AndroidContent {
    return AndroidContent(
        this.role,
        this.parts.map { it.toAndroidPart() }
    )
}

public fun Part.toAndroidPart(): AndroidPart {
    return when (this) {
        is TextPart -> AndroidTextPart(this.text)
        is FileDataPart -> AndroidFileDataPart(this.uri, this.mimeType)
        is InlineDataPart -> AndroidInlineDataPart(this.inlineData, this.mimeType)
        is ImagePart -> AndroidImagePart(this.image)
        else -> throw error("Unknown prompt part type")
    }
}
