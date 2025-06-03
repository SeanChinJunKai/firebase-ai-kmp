package io.github.seanchinjunkai.firebase.ai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import com.google.firebase.ai.FirebaseAI as AndroidFirebaseAI
import com.google.firebase.ai.GenerativeModel as AndroidGenerativeModel


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
    public actual suspend fun generateContent(prompt: String): GenerateContentResponse {
        val androidResponse = androidGenerativeModel.generateContent(prompt)
        return androidResponse.toGenerateContentResponse()
    }

    public actual suspend fun generateContent(vararg prompt: Content): GenerateContentResponse {
        val input = prompt.map { it.toAndroidContent() }.toTypedArray()
        val androidResponse = androidGenerativeModel.generateContent(*input)
        return androidResponse.toGenerateContentResponse()
    }

    public actual suspend fun countTokens(prompt: String): CountTokensResponse {
        return androidGenerativeModel.countTokens(prompt).toCountTokensResponse()
    }

    public actual suspend fun countTokens(vararg prompt: Content): CountTokensResponse {
        val input = prompt.map { it.toAndroidContent() }.toTypedArray()
        return androidGenerativeModel.countTokens(*input).toCountTokensResponse()
    }

}
