package io.github.seanchinjunkai.firebase.ai

import com.google.firebase.ai.type.generationConfig
import io.github.seanchinjunkai.firebase.ai.type.BlockReason
import io.github.seanchinjunkai.firebase.ai.type.Candidate
import io.github.seanchinjunkai.firebase.ai.type.Citation
import io.github.seanchinjunkai.firebase.ai.type.CitationMetadata
import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.ContentModality
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.Date
import io.github.seanchinjunkai.firebase.ai.type.FileDataPart
import io.github.seanchinjunkai.firebase.ai.type.FinishReason
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import io.github.seanchinjunkai.firebase.ai.type.GenerationConfig
import io.github.seanchinjunkai.firebase.ai.type.HarmBlockMethod
import io.github.seanchinjunkai.firebase.ai.type.HarmBlockThreshold
import io.github.seanchinjunkai.firebase.ai.type.HarmCategory
import io.github.seanchinjunkai.firebase.ai.type.HarmProbability
import io.github.seanchinjunkai.firebase.ai.type.HarmSeverity
import io.github.seanchinjunkai.firebase.ai.type.ImagePart
import io.github.seanchinjunkai.firebase.ai.type.InlineDataPart
import io.github.seanchinjunkai.firebase.ai.type.ModalityTokenCount
import io.github.seanchinjunkai.firebase.ai.type.Part
import io.github.seanchinjunkai.firebase.ai.type.PromptFeedback
import io.github.seanchinjunkai.firebase.ai.type.RequestOptions
import io.github.seanchinjunkai.firebase.ai.type.SafetyRating
import io.github.seanchinjunkai.firebase.ai.type.SafetySetting
import io.github.seanchinjunkai.firebase.ai.type.TextPart
import io.github.seanchinjunkai.firebase.ai.type.Tool
import io.github.seanchinjunkai.firebase.ai.type.ToolConfig
import io.github.seanchinjunkai.firebase.ai.type.UsageMetadata
import java.util.Calendar
import com.google.firebase.ai.type.CountTokensResponse as AndroidCountTokensResponse
import com.google.firebase.ai.type.ModalityTokenCount as AndroidModalityTokenCount
import com.google.firebase.ai.type.ContentModality as AndroidContentModality
import com.google.firebase.ai.type.GenerateContentResponse as AndroidGenerateContentResponse
import com.google.firebase.ai.type.Candidate as AndroidCandidate
import com.google.firebase.ai.type.PromptFeedback as AndroidPromptFeedback
import com.google.firebase.ai.type.UsageMetadata as AndroidUsageMetadata
import com.google.firebase.ai.type.BlockReason as AndroidBlockReason
import com.google.firebase.ai.type.SafetyRating as AndroidSafetyRating
import com.google.firebase.ai.type.HarmCategory as AndroidHarmCategory
import com.google.firebase.ai.type.HarmProbability as AndroidHarmProbability
import com.google.firebase.ai.type.HarmSeverity as AndroidHarmSeverity
import com.google.firebase.ai.type.FinishReason as AndroidFinishReason
import com.google.firebase.ai.type.CitationMetadata as AndroidCitationMetadata
import com.google.firebase.ai.type.Citation as AndroidCitation
import com.google.firebase.ai.type.Content as AndroidContent
import com.google.firebase.ai.type.Part as AndroidPart
import com.google.firebase.ai.type.TextPart as AndroidTextPart
import com.google.firebase.ai.type.ImagePart as AndroidImagePart
import com.google.firebase.ai.type.FileDataPart as AndroidFileDataPart
import com.google.firebase.ai.type.InlineDataPart as AndroidInlineDataPart
import com.google.firebase.ai.type.GenerationConfig as AndroidGenerationConfig
import com.google.firebase.ai.type.SafetySetting as AndroidSafetySetting
import com.google.firebase.ai.type.HarmBlockThreshold as AndroidHarmBlockThreshold
import com.google.firebase.ai.type.HarmBlockMethod as AndroidHarmBlockMethod
import com.google.firebase.ai.type.Tool as AndroidTool
import com.google.firebase.ai.type.ToolConfig as AndroidToolConfig
import com.google.firebase.ai.type.RequestOptions as AndroidRequestOptions

/* Mapping from firebase-android-sdk types to commonMain types */
public fun AndroidGenerateContentResponse.toGenerateContentResponse(): GenerateContentResponse {
    return GenerateContentResponse(
        candidates = this.candidates.map { it.toCandidate() },
        promptFeedback = this.promptFeedback?.toPromptFeedback(),
        usageMetadata = this.usageMetadata?.toUsageMetadata()
    )
}

public fun AndroidCandidate.toCandidate(): Candidate {
    return Candidate(
        content = this.content.toContent(),
        safetyRatings = this.safetyRatings.map { it.toSafetyRating() },
        citationMetadata = this.citationMetadata?.toCitationMetadata(),
        finishReason = this.finishReason?.toFinishReason()
    )
}

public fun AndroidContent.toContent(): Content {
    return Content(
        role = this.role,
        parts = this.parts.map { it.toPart() }
    )
}

public fun AndroidPart.toPart(): Part {
    return when(this) {
        is AndroidTextPart -> TextPart(this.text)
        is AndroidFileDataPart -> FileDataPart(this.uri, this.mimeType)
        is AndroidInlineDataPart -> InlineDataPart(this.inlineData, this.mimeType)
        is AndroidImagePart -> ImagePart(this.image)
        else -> throw error("Unknown prompt part type")
    }
}

public fun AndroidCitationMetadata.toCitationMetadata(): CitationMetadata {
    return CitationMetadata(
        citations = this.citations.map { it.toCitation() }
    )
}

public fun AndroidCitation.toCitation(): Citation {
    return Citation(
        title = this.title,
        startIndex = this.startIndex,
        endIndex = this.endIndex,
        uri = this.uri,
        license = this.license,
        publicationDate = Date(
            year = this.publicationDate?.get(Calendar.YEAR),
            month = this.publicationDate?.get(Calendar.MONTH)?.plus(1),
            day = this.publicationDate?.get(Calendar.DAY_OF_MONTH)
        )
    )
}

public fun AndroidPromptFeedback.toPromptFeedback(): PromptFeedback {
    return PromptFeedback(
        blockReason = this.blockReason?.toBlockReason(),
        safetyRatings = this.safetyRatings.map { it.toSafetyRating() },
        blockReasonMessage = this.blockReasonMessage
    )
}

public fun AndroidUsageMetadata.toUsageMetadata(): UsageMetadata {
    return UsageMetadata(
        promptTokenCount = this.promptTokenCount,
        candidatesTokenCount = this.candidatesTokenCount,
        totalTokenCount = this.totalTokenCount,
        promptTokensDetails = this.promptTokensDetails.map { it.toModalityTokenCount() },
        candidatesTokensDetails = this.candidatesTokensDetails.map { it.toModalityTokenCount() }
    )
}

public fun AndroidBlockReason.toBlockReason(): BlockReason {
    return BlockReason(
        name = this.name,
    )
}

public fun AndroidSafetyRating.toSafetyRating(): SafetyRating {
    return SafetyRating(
        category = this.category.toHarmCategory(),
        probability = this.probability.toHarmProbability(),
        probabilityScore = this.probabilityScore,
        blocked = this.blocked,
        severity = this.severity?.toHarmSeverity(),
        severityScore = this.severityScore
    )
}

public fun AndroidHarmCategory.toHarmCategory(): HarmCategory {
    return when (this) {
        AndroidHarmCategory.UNKNOWN -> HarmCategory.UNKNOWN
        AndroidHarmCategory.HARASSMENT -> HarmCategory.HARASSMENT
        AndroidHarmCategory.HATE_SPEECH -> HarmCategory.HATE_SPEECH
        AndroidHarmCategory.SEXUALLY_EXPLICIT -> HarmCategory.SEXUALLY_EXPLICIT
        AndroidHarmCategory.DANGEROUS_CONTENT -> HarmCategory.DANGEROUS_CONTENT
        AndroidHarmCategory.CIVIC_INTEGRITY -> HarmCategory.CIVIC_INTEGRITY
        else -> throw error("Unknown HarmCategory")
    }
}

public fun AndroidHarmProbability.toHarmProbability(): HarmProbability {
    return when (this) {
        AndroidHarmProbability.UNKNOWN -> HarmProbability(
            name = "UNKNOWN"
        )
        AndroidHarmProbability.NEGLIGIBLE -> HarmProbability(
            name = "NEGLIGIBLE"
        )
        AndroidHarmProbability.LOW -> HarmProbability(
            name = "LOW"
        )
        AndroidHarmProbability.MEDIUM -> HarmProbability(
            name = "MEDIUM"
        )
        AndroidHarmProbability.HIGH -> HarmProbability(
            name = "HIGH"
        )
        else -> throw error("Unknown HarmProbability")
    }
}
public fun AndroidHarmSeverity.toHarmSeverity(): HarmSeverity {
    return when (this) {
        AndroidHarmSeverity.UNKNOWN -> HarmSeverity(
            name = "UNKNOWN"
        )
        AndroidHarmSeverity.NEGLIGIBLE -> HarmSeverity(
            name = "NEGLIGIBLE"
        )
        AndroidHarmSeverity.LOW -> HarmSeverity(
            name = "LOW"
        )
        AndroidHarmSeverity.MEDIUM -> HarmSeverity(
            name = "MEDIUM"
        )
        AndroidHarmSeverity.HIGH -> HarmSeverity(
            name = "HIGH"
        )
        else -> throw error("Unknown HarmProbability")
    }
}

public fun AndroidFinishReason.toFinishReason(): FinishReason {
    return FinishReason(
        name = this.name,
    )
}


public fun AndroidCountTokensResponse.toCountTokensResponse(): CountTokensResponse {
    return CountTokensResponse(
        totalTokens = this.totalTokens,
        totalBillableCharacters = this.totalBillableCharacters,
        promptTokensDetails = this.promptTokensDetails.map { it.toModalityTokenCount() }
    )
}

public fun AndroidModalityTokenCount.toModalityTokenCount(): ModalityTokenCount {
    return ModalityTokenCount(
        modality = this.modality.toContentModality(),
        tokenCount = this.tokenCount
    )
}

public fun AndroidContentModality.toContentModality(): ContentModality {
    return ContentModality(
        ordinal = this.ordinal
    )
}

/* Mapping from commonMain types to firebase-android-sdk types */
public fun Content.toAndroidContent(): AndroidContent {
    return AndroidContent(
        this.role,
        this.parts.map { it.toAndroidPart() }
    )
}

public fun Part.toAndroidPart(): AndroidPart {
    return when (this) {
        is TextPart -> AndroidTextPart(this.text)
        is FileDataPart -> AndroidFileDataPart(this.uri, this.mimeType)
        is InlineDataPart -> AndroidInlineDataPart(this.inlineData, this.mimeType)
        is ImagePart -> AndroidImagePart(this.image)
        else -> throw error("Unknown prompt part type")
    }
}

public fun GenerationConfig.toAndroidGenerationConfig(): AndroidGenerationConfig {
    return generationConfig {
        setTemperature(this.temperature)
        setTopK(this.topK)
        setTopP(this.topP)
        setCandidateCount(this.candidateCount)
        setMaxOutputTokens(this.maxOutputTokens)
        setPresencePenalty(this.presencePenalty)
        setFrequencyPenalty(this.frequencyPenalty)
        setStopSequences(this.stopSequences)
        setResponseMimeType(this.responseMimeType)
        setResponseSchema(this.responseSchema)
        setResponseModalities(this.responseModalities)
    }
}

public fun SafetySetting.toAndroidSafetySetting(): AndroidSafetySetting {
    return AndroidSafetySetting(
        this.harmCategory.toAndroidHarmCategory(),
        this.threshold.toAndroidHarmBlockThreshold(),
        this.method?.toAndroidHarmBlockMethod()
    )
}

public fun HarmCategory.toAndroidHarmCategory(): AndroidHarmCategory {
    return when (this) {
        HarmCategory.UNKNOWN -> AndroidHarmCategory.UNKNOWN
        HarmCategory.HARASSMENT -> AndroidHarmCategory.HARASSMENT
        HarmCategory.HATE_SPEECH -> AndroidHarmCategory.HATE_SPEECH
        HarmCategory.SEXUALLY_EXPLICIT -> AndroidHarmCategory.SEXUALLY_EXPLICIT
        HarmCategory.DANGEROUS_CONTENT -> AndroidHarmCategory.DANGEROUS_CONTENT
        HarmCategory.CIVIC_INTEGRITY -> AndroidHarmCategory.CIVIC_INTEGRITY
        else -> throw error("Unknown HarmCategory")
    }
}

public fun HarmBlockThreshold.toAndroidHarmBlockThreshold(): AndroidHarmBlockThreshold {
    return when (this) {
        HarmBlockThreshold.LOW_AND_ABOVE -> AndroidHarmBlockThreshold.LOW_AND_ABOVE
        HarmBlockThreshold.MEDIUM_AND_ABOVE -> AndroidHarmBlockThreshold.MEDIUM_AND_ABOVE
        HarmBlockThreshold.ONLY_HIGH -> AndroidHarmBlockThreshold.ONLY_HIGH
        HarmBlockThreshold.NONE -> AndroidHarmBlockThreshold.NONE
        HarmBlockThreshold.OFF -> AndroidHarmBlockThreshold.OFF
        else -> throw error("Unknown HarmBlockThreshold")
    }
}

public fun HarmBlockMethod.toAndroidHarmBlockMethod(): AndroidHarmBlockMethod {
    return when (this) {
        HarmBlockMethod.SEVERITY -> AndroidHarmBlockMethod.SEVERITY
        HarmBlockMethod.PROBABILITY -> AndroidHarmBlockMethod.PROBABILITY
        else -> throw IllegalArgumentException("Unknown HarmBlockMethod")
    }
}

public fun Tool.toAndroidTool(): AndroidTool {
    TODO("Not yet implemented")
}

public fun ToolConfig.toAndroidToolConfig(): AndroidToolConfig {
    TODO("Not yet implemented")
}

public fun RequestOptions.toAndroidRequestOptions(): AndroidRequestOptions {
    return AndroidRequestOptions(
        this.timeout
    )
}