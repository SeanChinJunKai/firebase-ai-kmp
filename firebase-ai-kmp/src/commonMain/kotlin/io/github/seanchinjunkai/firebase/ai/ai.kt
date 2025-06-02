package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.Content


expect object Firebase {
    fun ai(backend: GenerativeBackendEnum): FirebaseAI
}


expect class FirebaseAI {
    fun generativeModel(modelName: String): GenerativeModel
}

expect class GenerativeModel {

    public suspend fun generateContent(prompt: String): String

    public suspend fun generateContent(vararg prompt: Content): String
}
