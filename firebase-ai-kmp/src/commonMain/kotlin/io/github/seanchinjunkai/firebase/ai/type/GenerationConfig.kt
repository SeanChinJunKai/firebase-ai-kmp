package io.github.seanchinjunkai.firebase.ai.type

public class GenerationConfig
private constructor(
    internal val temperature: Float?,
    internal val topK: Int?,
    internal val topP: Float?,
    internal val candidateCount: Int?,
    internal val maxOutputTokens: Int?,
    internal val presencePenalty: Float?,
    internal val frequencyPenalty: Float?,
    internal val stopSequences: List<String>?,
    internal val responseMimeType: String?,
    internal val responseSchema: Schema?,
    internal val responseModalities: List<ResponseModality>?,
) {
    public class Builder {
        public var temperature: Float? = null
        public var topK: Int? = null
        public var topP: Float? = null
        public var candidateCount: Int? = null
        public var maxOutputTokens: Int? = null
        public var presencePenalty: Float? = null
        public var frequencyPenalty: Float? = null
        public var stopSequences: List<String>? = null
        public var responseMimeType: String? = null
        public var responseSchema: Schema? = null
        public var responseModalities: List<ResponseModality>? = null

        public fun setTemperature(temperature: Float?): Builder = apply {
            this.temperature = temperature
        }
        public fun setTopK(topK: Int?): Builder = apply { this.topK = topK }
        public fun setTopP(topP: Float?): Builder = apply { this.topP = topP }
        public fun setCandidateCount(candidateCount: Int?): Builder = apply {
            this.candidateCount = candidateCount
        }
        public fun setMaxOutputTokens(maxOutputTokens: Int?): Builder = apply {
            this.maxOutputTokens = maxOutputTokens
        }
        public fun setPresencePenalty(presencePenalty: Float?): Builder = apply {
            this.presencePenalty = presencePenalty
        }
        public fun setFrequencyPenalty(frequencyPenalty: Float?): Builder = apply {
            this.frequencyPenalty = frequencyPenalty
        }
        public fun setStopSequences(stopSequences: List<String>?): Builder = apply {
            this.stopSequences = stopSequences
        }
        public fun setResponseMimeType(responseMimeType: String?): Builder = apply {
            this.responseMimeType = responseMimeType
        }
        public fun setResponseSchema(responseSchema: Schema?): Builder = apply {
            this.responseSchema = responseSchema
        }
        public fun setResponseModalities(responseModalities: List<ResponseModality>?): Builder = apply {
            this.responseModalities = responseModalities
        }

        public fun build(): GenerationConfig =
            GenerationConfig(
                temperature = temperature,
                topK = topK,
                topP = topP,
                candidateCount = candidateCount,
                maxOutputTokens = maxOutputTokens,
                stopSequences = stopSequences,
                presencePenalty = presencePenalty,
                frequencyPenalty = frequencyPenalty,
                responseMimeType = responseMimeType,
                responseSchema = responseSchema,
                responseModalities = responseModalities
            )
    }

    public companion object {
        public fun builder(): Builder = Builder()
    }
}

public fun generationConfig(init: GenerationConfig.Builder.() -> Unit): GenerationConfig {
    val builder = GenerationConfig.builder()
    builder.init()
    return builder.build()
}