package io.github.seanchinjunkai.firebase.ai


expect object Firebase {
    fun ai(backend: GenerativeBackendEnum): FirebaseAI
}


expect class FirebaseAI {
    fun generativeModel(modelName: String): GenerativeModel
}

expect class GenerativeModel {

    public suspend fun generateContent(prompt: String): String

    public suspend fun generateContentV2(prompt: String): String

    public suspend fun generateContent(vararg prompt: PromptPart): String
}
