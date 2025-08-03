package io.github.seanchinjunkai.firebase.ai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import io.github.seanchinjunkai.firebase.ai.type.UnknownException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.google.firebase.ai.FirebaseAI as AndroidFirebaseAI
import com.google.firebase.ai.GenerativeModel as AndroidGenerativeModel
import com.google.firebase.ai.type.FirebaseAIException as AndroidFirebaseAIException


public actual object Firebase {
    // TODO: App parameter currently missing
    public actual fun ai(backend: GenerativeBackend): FirebaseAI {
        return when (backend.backend)  {
            GenerativeBackendEnum.GOOGLE_AI -> FirebaseAI(Firebase.ai(backend = com.google.firebase.ai.type.GenerativeBackend.googleAI()))
            GenerativeBackendEnum.VERTEX_AI -> FirebaseAI(Firebase.ai(backend = com.google.firebase.ai.type.GenerativeBackend.vertexAI(backend.location)))
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
        try {
            val androidResponse = androidGenerativeModel.generateContent(prompt)
            return androidResponse.toGenerateContentResponse()
        } catch(e: AndroidFirebaseAIException) {
            throw e.toFirebaseAIException()
        }
    }

    public actual fun generateContentStream(prompt: String): Flow<GenerateContentResponse> {
        val androidFlowResponse = androidGenerativeModel.generateContentStream(prompt)
        return androidFlowResponse
            .catch { throwable ->
                val firebaseException = throwable as? AndroidFirebaseAIException
                throw firebaseException?.toFirebaseAIException() ?: UnknownException(throwable.message ?: "", throwable.cause)
            }
            .map {
                it.toGenerateContentResponse()
            }
    }

    public actual suspend fun generateContent(vararg prompt: Content): GenerateContentResponse {
        try {
            val input = prompt.map { it.toAndroidContent() }.toTypedArray()
            val androidResponse = androidGenerativeModel.generateContent(*input)
            return androidResponse.toGenerateContentResponse()
        } catch (e: AndroidFirebaseAIException) {
            throw e.toFirebaseAIException()
        }
    }

    public actual fun generateContentStream(vararg prompt: Content): Flow<GenerateContentResponse> {
        val input = prompt.map { it.toAndroidContent() }.toTypedArray()
        val androidFlowResponse = androidGenerativeModel.generateContentStream(*input)
        return androidFlowResponse
            .catch { throwable ->
                val firebaseException = throwable as? AndroidFirebaseAIException
                throw firebaseException?.toFirebaseAIException() ?: UnknownException(throwable.message ?: "", throwable.cause)
            }
            .map {
                it.toGenerateContentResponse()
            }

    }

    public actual suspend fun countTokens(prompt: String): CountTokensResponse {
        try {
            return androidGenerativeModel.countTokens(prompt).toCountTokensResponse()
        } catch (e: AndroidFirebaseAIException) {
            throw e.toFirebaseAIException()
        }
    }

    public actual suspend fun countTokens(vararg prompt: Content): CountTokensResponse {
        try {
            val input = prompt.map { it.toAndroidContent() }.toTypedArray()
            return androidGenerativeModel.countTokens(*input).toCountTokensResponse()
        } catch (e: AndroidFirebaseAIException) {
            throw e.toFirebaseAIException()
        }
    }

    public actual fun startChat(history: List<Content>): Chat =
        Chat(this, history.toMutableList())
}
