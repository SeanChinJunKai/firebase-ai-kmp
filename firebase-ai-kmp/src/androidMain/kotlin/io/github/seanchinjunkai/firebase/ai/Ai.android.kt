package io.github.seanchinjunkai.firebase.ai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerationConfig
import io.github.seanchinjunkai.firebase.ai.type.GenerativeBackend
import io.github.seanchinjunkai.firebase.ai.type.RequestOptions
import io.github.seanchinjunkai.firebase.ai.type.SafetySetting
import io.github.seanchinjunkai.firebase.ai.type.Tool
import io.github.seanchinjunkai.firebase.ai.type.ToolConfig
import io.github.seanchinjunkai.firebase.ai.type.UnknownException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.google.firebase.ai.FirebaseAI as AndroidFirebaseAI
import com.google.firebase.ai.GenerativeModel as AndroidGenerativeModel
import com.google.firebase.ai.type.FirebaseAIException as AndroidFirebaseAIException
import com.google.firebase.ai.type.RequestOptions as AndroidRequestOptions


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
    public actual fun generativeModel(
        modelName: String,
        generationConfig: GenerationConfig?,
        safetySettings: List<SafetySetting>?,
        tools: List<Tool>?,
        toolConfig: ToolConfig?,
        systemInstruction: Content?,
        requestOptions: RequestOptions
    ): GenerativeModel {
        return GenerativeModel(
            androidFirebaseAI.generativeModel(
                modelName,
                generationConfig?.toAndroidGenerationConfig(),
                safetySettings?.map { it.toAndroidSafetySetting() },
                tools?.map { it.toAndroidTool() },
                toolConfig?.toAndroidToolConfig(),
                systemInstruction?.toAndroidContent(),
                requestOptions.toAndroidRequestOptions()
            )
        )
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
}
