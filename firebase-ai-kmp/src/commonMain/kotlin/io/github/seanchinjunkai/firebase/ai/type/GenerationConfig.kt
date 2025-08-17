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
    internal val thinkingConfig: ThinkingConfig?,
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
        public var thinkingConfig: ThinkingConfig? = null

        public fun setTemperature(temperature: Float?): GenerationConfig.Builder = apply {
            this.temperature = temperature
        }

        public fun setTopK(topK: Int?): GenerationConfig.Builder = apply { this.topK = topK }
        public fun setTopP(topP: Float?): GenerationConfig.Builder = apply { this.topP = topP }
        public fun setCandidateCount(candidateCount: Int?): GenerationConfig.Builder = apply {
            this.candidateCount = candidateCount
        }

        public fun setMaxOutputTokens(maxOutputTokens: Int?): GenerationConfig.Builder = apply {
            this.maxOutputTokens = maxOutputTokens
        }

        public fun setPresencePenalty(presencePenalty: Float?): GenerationConfig.Builder = apply {
            this.presencePenalty = presencePenalty
        }

        public fun setFrequencyPenalty(frequencyPenalty: Float?): GenerationConfig.Builder = apply {
            this.frequencyPenalty = frequencyPenalty
        }

        public fun setStopSequences(stopSequences: List<String>?): GenerationConfig.Builder =
            apply {
                this.stopSequences = stopSequences
            }

        public fun setResponseMimeType(responseMimeType: String?): GenerationConfig.Builder =
            apply {
                this.responseMimeType = responseMimeType
            }

        public fun setResponseSchema(responseSchema: Schema?): GenerationConfig.Builder = apply {
            this.responseSchema = responseSchema
        }

        public fun setResponseModalities(responseModalities: List<ResponseModality>?): GenerationConfig.Builder =
            apply {
                this.responseModalities = responseModalities
            }

        public fun setThinkingConfig(thinkingConfig: ThinkingConfig?): GenerationConfig.Builder =
            apply {
                this.thinkingConfig = thinkingConfig
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
                responseModalities = responseModalities,
                thinkingConfig = thinkingConfig
            )
    }

    public companion object {
        public fun builder(): GenerationConfig.Builder = Builder()
    }
}

public fun generationConfig(init: GenerationConfig.Builder.() -> Unit): GenerationConfig {
    val builder = GenerationConfig.builder()
    builder.init()
    return builder.build()
}