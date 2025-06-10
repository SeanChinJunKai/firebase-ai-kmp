package io.github.seanchinjunkai.firebase.ai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.google.firebase.ai.FirebaseAI as AndroidFirebaseAI
import com.google.firebase.ai.GenerativeModel as AndroidGenerativeModel


public actual object Firebase {
    // TODO: App parameter currently missing
    public actual fun ai(backend: GenerativeBackend): FirebaseAI {
        return when (backend)  {
            GenerativeBackend.GOOGLE_AI -> FirebaseAI(Firebase.ai(backend = com.google.firebase.ai.type.GenerativeBackend.googleAI()))
            GenerativeBackend.VERTEX_AI -> FirebaseAI(Firebase.ai(backend = com.google.firebase.ai.type.GenerativeBackend.vertexAI()))
        }
    }
}



actual class FirebaseAI internal constructor(internal val androidFirebaseAI: AndroidFirebaseAI) {
    // TODO: Add missing parameters
    public actual fun generativeModel(modelName: String): IGenerativeModel {
        return GenerativeModel(androidFirebaseAI.generativeModel(modelName))
    }
}


class GenerativeModel internal constructor(internal val androidGenerativeModel: AndroidGenerativeModel): IGenerativeModel {
    public override suspend fun generateContent(prompt: String): GenerateContentResponse {
        val androidResponse = androidGenerativeModel.generateContent(prompt)
        return androidResponse.toGenerateContentResponse()
    }

    public override fun generateContentStream(prompt: String): Flow<GenerateContentResponse> {
        val androidFlowResponse = androidGenerativeModel.generateContentStream(prompt)
        return androidFlowResponse.map {
            it.toGenerateContentResponse()
        }
    }

    public override suspend fun generateContent(vararg prompt: Content): GenerateContentResponse {
        val input = prompt.map { it.toAndroidContent() }.toTypedArray()
        val androidResponse = androidGenerativeModel.generateContent(*input)
        return androidResponse.toGenerateContentResponse()
    }

    public override fun generateContentStream(vararg prompt: Content): Flow<GenerateContentResponse> {
        val input = prompt.map { it.toAndroidContent() }.toTypedArray()
        val androidFlowResponse = androidGenerativeModel.generateContentStream(*input)
        return androidFlowResponse.map {
            it.toGenerateContentResponse()
        }
    }

    public override suspend fun countTokens(prompt: String): CountTokensResponse {
        return androidGenerativeModel.countTokens(prompt).toCountTokensResponse()
    }

    public override suspend fun countTokens(vararg prompt: Content): CountTokensResponse {
        val input = prompt.map { it.toAndroidContent() }.toTypedArray()
        return androidGenerativeModel.countTokens(*input).toCountTokensResponse()
    }

}
