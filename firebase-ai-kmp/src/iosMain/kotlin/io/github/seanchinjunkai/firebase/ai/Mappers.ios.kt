@file:OptIn(ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import cocoapods.FirebaseAIBridge.FileDataPartObjc
import cocoapods.FirebaseAIBridge.ImagePartObjc
import cocoapods.FirebaseAIBridge.InlineDataPartObjc
import cocoapods.FirebaseAIBridge.ModelContentObjc
import cocoapods.FirebaseAIBridge.PartObjc
import cocoapods.FirebaseAIBridge.TextPartObjc
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
import io.github.seanchinjunkai.firebase.ai.type.HarmCategory
import io.github.seanchinjunkai.firebase.ai.type.HarmProbability
import io.github.seanchinjunkai.firebase.ai.type.HarmSeverity
import io.github.seanchinjunkai.firebase.ai.type.ImagePart
import io.github.seanchinjunkai.firebase.ai.type.InlineDataPart
import io.github.seanchinjunkai.firebase.ai.type.ModalityTokenCount
import io.github.seanchinjunkai.firebase.ai.type.Part
import io.github.seanchinjunkai.firebase.ai.type.PromptFeedback
import io.github.seanchinjunkai.firebase.ai.type.SafetyRating
import io.github.seanchinjunkai.firebase.ai.type.TextPart
import io.github.seanchinjunkai.firebase.ai.type.UsageMetadata
import kotlinx.cinterop.ExperimentalForeignApi
import cocoapods.FirebaseAIBridge.CountTokensResponseObjc as iOSCountTokensResponse
import cocoapods.FirebaseAIBridge.ModalityTokenCountObjc as iOSModalityTokenCount
import cocoapods.FirebaseAIBridge.ContentModalityObjc as iOSContentModality
import cocoapods.FirebaseAIBridge.GenerateContentResponseObjc as iOSGenerateContentResponse
import cocoapods.FirebaseAIBridge.CandidateObjc as iOSCandidate
import cocoapods.FirebaseAIBridge.ModelContentObjc as iOSContent
import cocoapods.FirebaseAIBridge.PartObjc as iOSPart
import cocoapods.FirebaseAIBridge.TextPartObjc as iOSTextPart
import cocoapods.FirebaseAIBridge.ImagePartObjc as iOSImagePart
import cocoapods.FirebaseAIBridge.InlineDataPartObjc as iOSInlineDataPart
import cocoapods.FirebaseAIBridge.FileDataPartObjc as iOSFileDataPart
import cocoapods.FirebaseAIBridge.CitationMetadataObjc as iOSCitationMetadata
import cocoapods.FirebaseAIBridge.CitationObjc as iOSCitation
import cocoapods.FirebaseAIBridge.PromptFeedbackObjc as iOSPromptFeedback
import cocoapods.FirebaseAIBridge.UsageMetadataObjc as iOSUsageMetadata
import cocoapods.FirebaseAIBridge.SafetyRatingObjc as iOSSafetyRating
import cocoapods.FirebaseAIBridge.BlockReasonObjc as iOSBlockReason
import cocoapods.FirebaseAIBridge.FinishReasonObjc as iOSFinishReason
import cocoapods.FirebaseAIBridge.HarmCategoryObjc as iOSHarmCategory
import cocoapods.FirebaseAIBridge.HarmProbabilityObjc as iOSHarmProbability
import cocoapods.FirebaseAIBridge.HarmSeverityObjc as iOSHarmSeverity

/* Mapping from firebase-ios-sdk types to commonMain types */
public fun iOSGenerateContentResponse.toGenerateContentResponse(): GenerateContentResponse {
    return GenerateContentResponse(
        candidates = this.candidates().map {
            val candidate = it as iOSCandidate
            candidate.toCandidate()
        },
        promptFeedback = this.promptFeedback()?.toPromptFeedback(),
        usageMetadata = this.usageMetadata()?.toUsageMetadata()
    )
}

public fun iOSCandidate.toCandidate(): Candidate {
    return Candidate(
        content = this.content().toContent(),
        safetyRatings = this.safetyRatings().map {
            val rating = it as iOSSafetyRating
            rating.toSafetyRating()
        },
        citationMetadata = this.citationMetadata()?.toCitationMetadata(),
        finishReason = this.finishReason()?.toFinishReason()
    )
}

public fun iOSContent.toContent(): Content {
    return Content(
        role = this.role(),
        parts = this.parts().map {
            val part = it as iOSPart
            part.toPart()
        }
    )
}

public fun iOSPart.toPart(): Part {
    return when(this) {
        is iOSTextPart -> TextPart(this.text())
        is iOSFileDataPart -> FileDataPart(this.fileURI(), this.mimeType())
        is iOSInlineDataPart -> InlineDataPart(this.data().toByteArray(), this.mimeType())
        is iOSImagePart -> ImagePart(this.image())
        else -> throw error("Unknown prompt part type")
    }
}

public fun iOSCitationMetadata.toCitationMetadata(): CitationMetadata {
    return CitationMetadata(
        citations = this.citations().map {
            val citation = it as iOSCitation
            citation.toCitation()
        }
    )
}

public fun iOSCitation.toCitation(): Citation {
    return Citation(
        title = this.title(),
        startIndex = this.startIndex().toInt(),
        endIndex = this.endIndex().toInt(),
        uri = this.uri(),
        license = this.license(),
        publicationDate = Date(
            year = this.publicationDate()?.year()?.toInt(),
            month = this.publicationDate()?.month()?.toInt(),
            day = this.publicationDate()?.day()?.toInt()
        )
    )
}

public fun iOSPromptFeedback.toPromptFeedback(): PromptFeedback {
    return PromptFeedback(
        blockReason = this.blockReason()?.toBlockReason(),
        safetyRatings = this.safetyRatings().map {
            val rating = it as iOSSafetyRating
            rating.toSafetyRating()
        },
        blockReasonMessage = this.blockReasonMessage()
    )
}

public fun iOSUsageMetadata.toUsageMetadata(): UsageMetadata {
    return UsageMetadata(
        promptTokenCount = this.promptTokenCount().toInt(),
        candidatesTokenCount = this.candidatesTokenCount().toInt(),
        totalTokenCount = this.totalTokenCount().toInt(),
        promptTokensDetails = this.promptTokensDetails().map {
            val tokenCount = it as iOSModalityTokenCount
            tokenCount.toModalityTokenCount()
        },
        candidatesTokensDetails = this.candidatesTokensDetails().map {
            val tokenCount = it as iOSModalityTokenCount
            tokenCount.toModalityTokenCount()
        }
    )
}

public fun iOSSafetyRating.toSafetyRating(): SafetyRating {
    return SafetyRating(
        category = this.category().toHarmCategory(),
        probability = this.probability().toHarmProbability(),
        probabilityScore = this.probabilityScore(),
        blocked = this.blocked(),
        severity = this.severity().toHarmSeverity(),
        severityScore = this.severityScore()
    )
}

public fun iOSFinishReason.toFinishReason(): FinishReason {
    return when (this.rawValue()) {
        "STOP" -> FinishReason.STOP
        "MAX_TOKENS" -> FinishReason.MAX_TOKENS
        "SAFETY" -> FinishReason.SAFETY
        "RECITATION" -> FinishReason.RECITATION
        "OTHER" -> FinishReason.OTHER
        "BLOCKLIST" -> FinishReason.BLOCKLIST
        "PROHIBITED_CONTENT" -> FinishReason.PROHIBITED_CONTENT
        "SPII" -> FinishReason.SPII
        "MALFORMED_FUNCTION_CALL" -> FinishReason.MALFORMED_FUNCTION_CALL
        else -> FinishReason.UNKNOWN
    }
}

public fun iOSBlockReason.toBlockReason(): BlockReason {
    return when (this.rawValue()) {
        "SAFETY" -> BlockReason.SAFETY
        "OTHER" -> BlockReason.OTHER
        "BLOCKLIST" -> BlockReason.BLOCKLIST
        "PROHIBITED_CONTENT" -> BlockReason.PROHIBITED_CONTENT
        else -> BlockReason.UNKNOWN
    }
}

public fun iOSHarmCategory.toHarmCategory(): HarmCategory {
    return when (this.rawValue()) {
        "HARM_CATEGORY_HARASSMENT" -> HarmCategory.HARASSMENT
        "HARM_CATEGORY_HATE_SPEECH" -> HarmCategory.HATE_SPEECH
        "HARM_CATEGORY_SEXUALLY_EXPLICIT" -> HarmCategory.SEXUALLY_EXPLICIT
        "HARM_CATEGORY_DANGEROUS_CONTENT" -> HarmCategory.DANGEROUS_CONTENT
        "HARM_CATEGORY_CIVIC_INTEGRITY" -> HarmCategory.CIVIC_INTEGRITY
        else -> HarmCategory.UNKNOWN
    }
}

public fun iOSHarmProbability.toHarmProbability(): HarmProbability {
    return when (this.rawValue()) {
        "NEGLIGIBLE" -> HarmProbability.NEGLIGIBLE
        "LOW" -> HarmProbability.LOW
        "MEDIUM" -> HarmProbability.MEDIUM
        "HIGH" -> HarmProbability.HIGH
        else -> HarmProbability.UNKNOWN
    }
}

public fun iOSHarmSeverity.toHarmSeverity(): HarmSeverity {
    return when (this.rawValue()) {
        "HARM_SEVERITY_NEGLIGIBLE" -> HarmSeverity.NEGLIGIBLE
        "HARM_SEVERITY_LOW" -> HarmSeverity.LOW
        "HARM_SEVERITY_MEDIUM" -> HarmSeverity.MEDIUM
        "HARM_SEVERITY_HIGH" -> HarmSeverity.HIGH
        else -> HarmSeverity.UNKNOWN
    }
}



public fun iOSCountTokensResponse.toCountTokensResponse(): CountTokensResponse {
    return CountTokensResponse(
        totalTokens = this.totalTokens().toInt(),
        promptTokensDetails = this.promptTokenDetails().map {
            val tokenCount = it as iOSModalityTokenCount
            tokenCount.toModalityTokenCount()
        }
    )
}

public fun iOSModalityTokenCount.toModalityTokenCount(): ModalityTokenCount {
    return ModalityTokenCount(
        modality = this.modality().toContentModality(),
        tokenCount = this.tokenCount().toInt()
    )
}

public fun iOSContentModality.toContentModality(): ContentModality {
    return when (this.rawValue()) {
        "TEXT" -> ContentModality.TEXT
        "IMAGE" -> ContentModality.IMAGE
        "VIDEO" -> ContentModality.VIDEO
        "AUDIO" -> ContentModality.AUDIO
        "DOCUMENT" -> ContentModality.DOCUMENT
        else -> ContentModality.UNSPECIFIED
    }
}


/* Mapping from commonMain types to firebase-ios-sdk types */
public fun Content.toiOSContent(): ModelContentObjc {
    return ModelContentObjc(
        this.role,
        this.parts.map { it.toiOSPart() }
    )
}

public fun Part.toiOSPart(): PartObjc {
    return when (this) {
        is TextPart -> TextPartObjc(this.text)
        is FileDataPart -> FileDataPartObjc(this.uri, this.mimeType)
        is InlineDataPart -> InlineDataPartObjc(this.inlineData.toNSData(), this.mimeType)
        is ImagePart -> ImagePartObjc(this.image)
        else -> throw error("Unknown prompt part type")
    }
}