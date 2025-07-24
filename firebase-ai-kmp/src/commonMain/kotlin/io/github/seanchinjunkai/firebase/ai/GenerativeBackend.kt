package io.github.seanchinjunkai.firebase.ai

public class GenerativeBackend
internal constructor(internal val location: String, internal val backend: GenerativeBackendEnum) {
    public companion object {
        public fun googleAI() =
            GenerativeBackend("", GenerativeBackendEnum.GOOGLE_AI)

        public fun vertexAI(location: String = "us-central1"): GenerativeBackend {
            return GenerativeBackend(location, GenerativeBackendEnum.VERTEX_AI)
        }
    }
}

internal enum class GenerativeBackendEnum {
    GOOGLE_AI,
    VERTEX_AI,
}