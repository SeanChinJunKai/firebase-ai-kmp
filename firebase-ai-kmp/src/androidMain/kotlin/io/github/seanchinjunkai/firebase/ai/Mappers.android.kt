package io.github.seanchinjunkai.firebase.ai

import io.github.seanchinjunkai.firebase.ai.type.BlockReason
import io.github.seanchinjunkai.firebase.ai.type.Candidate
import io.github.seanchinjunkai.firebase.ai.type.Citation
import io.github.seanchinjunkai.firebase.ai.type.CitationMetadata
import io.github.seanchinjunkai.firebase.ai.type.Content
import io.github.seanchinjunkai.firebase.ai.type.ContentModality
import io.github.seanchinjunkai.firebase.ai.type.CountTokensResponse
import io.github.seanchinjunkai.firebase.ai.type.Date
import io.github.seanchinjunkai.firebase.ai.type.FinishReason
import io.github.seanchinjunkai.firebase.ai.type.GenerateContentResponse
import io.github.seanchinjunkai.firebase.ai.type.HarmCategory
import io.github.seanchinjunkai.firebase.ai.type.HarmProbability
import io.github.seanchinjunkai.firebase.ai.type.HarmSeverity
import io.github.seanchinjunkai.firebase.ai.type.ModalityTokenCount
import io.github.seanchinjunkai.firebase.ai.type.PromptFeedback
import io.github.seanchinjunkai.firebase.ai.type.SafetyRating
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
        AndroidHarmCategory.UNKNOWN -> HarmCategory(
            name = "UNKNOWN"
        )
        AndroidHarmCategory.HARASSMENT -> HarmCategory(
            name = "HARASSMENT"
        )
        AndroidHarmCategory.HATE_SPEECH -> HarmCategory(
            name = "HATE_SPEECH"
        )
        AndroidHarmCategory.SEXUALLY_EXPLICIT -> HarmCategory(
            name = "SEXUALLY_EXPLICIT"
        )
        AndroidHarmCategory.DANGEROUS_CONTENT -> HarmCategory(
            name = "DANGEROUS_CONTENT"
        )
        AndroidHarmCategory.CIVIC_INTEGRITY -> HarmCategory(
            name = "CIVIC_INTEGRITY"
        )
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
