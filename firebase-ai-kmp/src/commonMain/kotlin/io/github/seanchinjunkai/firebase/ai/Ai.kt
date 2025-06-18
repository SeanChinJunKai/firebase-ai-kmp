package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerationConfig
import io.github.seanchinjunkai.firebase.ai.type.GenerativeBackend
import io.github.seanchinjunkai.firebase.ai.type.RequestOptions
import io.github.seanchinjunkai.firebase.ai.type.SafetySetting
import io.github.seanchinjunkai.firebase.ai.type.Tool
import io.github.seanchinjunkai.firebase.ai.type.ToolConfig
import kotlinx.coroutines.flow.Flow


expect object Firebase {
    fun ai(backend: GenerativeBackend): FirebaseAI
}


expect class FirebaseAI {
    fun generativeModel(
        modelName: String,
        generationConfig: GenerationConfig? = null,
        safetySettings: List<SafetySetting>? = null,
        tools: List<Tool>? = null,
        toolConfig: ToolConfig? = null,
        systemInstruction: Content? = null,
        requestOptions: RequestOptions = RequestOptions()
    ): GenerativeModel
}

expect class GenerativeModel {

    public suspend fun generateContent(prompt: String): GenerateContentResponse

    public fun generateContentStream(prompt: String): Flow<GenerateContentResponse>

    public suspend fun generateContent(vararg prompt: Content): GenerateContentResponse

    public fun generateContentStream(vararg prompt: Content): Flow<GenerateContentResponse>

    public suspend fun countTokens(prompt: String): CountTokensResponse

    public suspend fun countTokens(vararg prompt: Content): CountTokensResponse
}
