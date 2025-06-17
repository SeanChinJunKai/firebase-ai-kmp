@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import cocoapods.FirebaseAIBridge.CandidateObjc
import cocoapods.FirebaseAIBridge.GenerateContentResponseObjc
import cocoapods.FirebaseAIBridge.PromptFeedbackObjc
import cocoapods.FirebaseAIBridge.UsageMetadataObjc
import cocoapods.FirebaseAIBridge.SafetyRatingObjc
import cocoapods.FirebaseAIBridge.ModelContentObjc
import cocoapods.FirebaseAIBridge.FinishReasonObjc
import cocoapods.FirebaseAIBridge.CitationMetadataObjc
import cocoapods.FirebaseAIBridge.BlockReasonObjc
import cocoapods.FirebaseAIBridge.ModalityTokenCountObjc
import cocoapods.FirebaseAIBridge.PartObjc
import cocoapods.FirebaseAIBridge.TextPartObjc
import cocoapods.FirebaseAIBridge.HarmCategoryObjc
import cocoapods.FirebaseAIBridge.HarmProbabilityObjc
import cocoapods.FirebaseAIBridge.HarmSeverityObjc
import cocoapods.FirebaseAIBridge.CitationObjc
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import platform.Foundation.NSDateComponents
import kotlin.experimental.ExperimentalNativeApi

object GenerateContentResponseObjcSerializer : KSerializer<GenerateContentResponseObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("GenerateContentResponseObjc") {
        element("candidates", ListSerializer(CandidateObjcSerializer).descriptor)
        element("promptFeedback", PromptFeedbackObjcSerializer.descriptor)
        element("usageMetadata", UsageMetadataObjcSerializer.descriptor)
    }

    override fun serialize(
        encoder: Encoder,
        value: GenerateContentResponseObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): GenerateContentResponseObjc {
        var candidates: List<CandidateObjc> = emptyList()
        var promptFeedback: PromptFeedbackObjc? = null
        var usageMetadata: UsageMetadataObjc? = null

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> candidates = decodeSerializableElement(
                        descriptor,
                        index,
                        ListSerializer(CandidateObjcSerializer)
                    )
                    1 ->  promptFeedback = decodeSerializableElement(
                        descriptor,
                        index,
                        PromptFeedbackObjcSerializer
                    )
                    2 -> usageMetadata = decodeSerializableElement(
                        descriptor,
                        index,
                        UsageMetadataObjcSerializer
                    )
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        return GenerateContentResponseObjc(
            candidates = candidates,
            promptFeedback = promptFeedback,
            usageMetadata = usageMetadata
        )
    }

}

object CandidateObjcSerializer: KSerializer<CandidateObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CandidateObjc") {
        element("content", ListSerializer(ModelContentObjcSerializer).descriptor)
        element("safetyRatings", ListSerializer(SafetyRatingObjcSerializer).descriptor)
        element<String>("finishReason")
        element("citationMetadata", CitationMetadataObjcSerializer.descriptor)
    }
    override fun serialize(
        encoder: Encoder,
        value: CandidateObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): CandidateObjc {
        lateinit var content: ModelContentObjc
        var safetyRatings: List<SafetyRatingObjc> = emptyList()
        var finishReason: String? = null
        var citationMetadata: CitationMetadataObjc? = null

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> content = decodeSerializableElement(
                        descriptor,
                        index,
                        ModelContentObjcSerializer
                    )
                    1 ->  safetyRatings = decodeSerializableElement(
                        descriptor,
                        index,
                        ListSerializer(SafetyRatingObjcSerializer)
                    )
                    2 -> finishReason = decodeStringElement(descriptor, index)
                    3 -> citationMetadata = decodeSerializableElement(
                        descriptor,
                        index,
                        CitationMetadataObjcSerializer
                    )
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }


        return CandidateObjc(
            content = content,
            safetyRatings = safetyRatings,
            finishReason = finishReason?.let { FinishReasonObjc(it) },
            citationMetadata = citationMetadata
        )
    }
}



object PromptFeedbackObjcSerializer: KSerializer<PromptFeedbackObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PromptFeedbackObjc") {
        element<String>("blockReason")
        element("safetyRatings", ListSerializer(SafetyRatingObjcSerializer).descriptor)
        element<String>("blockReasonMessage")
    }

    override fun serialize(
        encoder: Encoder,
        value: PromptFeedbackObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): PromptFeedbackObjc {
        var blockReason: String? = null
        var safetyRatings: List<SafetyRatingObjc> = emptyList()
        var blockReasonMessage: String? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> blockReason = decodeStringElement(descriptor, index)
                    1 ->  safetyRatings = decodeSerializableElement(
                        descriptor,
                        index,
                        ListSerializer(SafetyRatingObjcSerializer)
                    )
                    2 -> blockReasonMessage = decodeStringElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        return PromptFeedbackObjc(
            blockReason = blockReason?.let { BlockReasonObjc(it) },
            safetyRatings = safetyRatings,
            blockReasonMessage = blockReasonMessage
        )
    }
}

object UsageMetadataObjcSerializer: KSerializer<UsageMetadataObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("UsageMetadataObjc") {
        element<Long>("promptTokenCount")
        element<Long>("candidatesTokenCount")
        element<Long>("totalTokenCount")
        element("promptTokensDetails", ListSerializer(ModalityTokenCountObjcSerializer).descriptor)
        element("candidatesTokensDetails", ListSerializer(ModalityTokenCountObjcSerializer).descriptor)
    }

    override fun serialize(
        encoder: Encoder,
        value: UsageMetadataObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): UsageMetadataObjc {
        var promptTokenCount = 0L
        var candidatesTokenCount = 0L
        var totalTokenCount = 0L
        var promptTokensDetails: List<ModalityTokenCountObjc> = emptyList()
        var candidatesTokensDetails: List<ModalityTokenCountObjc> = emptyList()

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> promptTokenCount = decodeLongElement(descriptor, index)
                    1 ->  candidatesTokenCount =decodeLongElement(descriptor, index)
                    2 -> totalTokenCount = decodeLongElement(descriptor, index)
                    3 -> promptTokensDetails = decodeSerializableElement(
                        descriptor,
                        index,
                        ListSerializer(ModalityTokenCountObjcSerializer)
                    )
                    4 -> candidatesTokensDetails = decodeSerializableElement(
                        descriptor,
                        index,
                        ListSerializer(ModalityTokenCountObjcSerializer)
                    )
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        return UsageMetadataObjc(
            promptTokenCount = promptTokenCount,
            candidatesTokenCount = candidatesTokenCount,
            totalTokenCount = totalTokenCount,
            promptTokensDetails = promptTokensDetails,
            candidatesTokensDetails = candidatesTokensDetails
        )
    }
}

object ModelContentObjcSerializer: KSerializer<ModelContentObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ModelContentObjc") {
        element<String>("role")
        element("parts", ListSerializer(PartObjcSerializer).descriptor)
    }

    override fun serialize(
        encoder: Encoder,
        value: ModelContentObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): ModelContentObjc {
        var role: String? = null
        var parts: List<PartObjc> = emptyList()

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> role = decodeStringElement(descriptor, index)
                    1 -> parts = decodeSerializableElement(
                        descriptor,
                        index,
                        ListSerializer(PartObjcSerializer)
                    )
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }

        return ModelContentObjc(
            role = role,
            parts = parts
        )
    }
}

object SafetyRatingObjcSerializer: KSerializer<SafetyRatingObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("SafetyRatingObjc") {
        element<String>("category")
        element<String>("probability")
        element<Float>("probabilityScore")
        element<String>("severity")
        element<Float>("severityScore")
        element<Boolean>("blocked")
    }

    override fun serialize(
        encoder: Encoder,
        value: SafetyRatingObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): SafetyRatingObjc {
        var category = ""
        var probability = ""
        var probabilityScore = 0F
        var severity = ""
        var severityScore = 0F
        var blocked = true

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> category = decodeStringElement(descriptor, index)
                    1 -> probability = decodeStringElement(descriptor, index)
                    2 -> probabilityScore = decodeFloatElement(descriptor, index)
                    3 -> severity = decodeStringElement(descriptor, index)
                    4 -> severityScore = decodeFloatElement(descriptor, index)
                    5 -> blocked = decodeBooleanElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }

        return SafetyRatingObjc(
            category = HarmCategoryObjc(category),
            probability = HarmProbabilityObjc(probability),
            probabilityScore = probabilityScore,
            severity = HarmSeverityObjc(severity),
            severityScore = severityScore,
            blocked = blocked
        )
    }

}


object CitationMetadataObjcSerializer: KSerializer<CitationMetadataObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CitationMetadataObjc") {
        element("citations", ListSerializer(CitationObjcSerializer).descriptor)
    }

    override fun serialize(
        encoder: Encoder,
        value: CitationMetadataObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): CitationMetadataObjc {
        var citations: List<CitationObjc> = emptyList()
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> citations = decodeSerializableElement(
                        ModelContentObjcSerializer.descriptor,
                        index,
                        ListSerializer(CitationObjcSerializer)
                    )
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        return CitationMetadataObjc(
            citations = citations
        )
    }
}

// TODO: Might need to account for union type in future https://cloud.google.com/vertex-ai/docs/reference/rest/v1/Content#Part
object PartObjcSerializer: KSerializer<PartObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PartObjc") {
        element<String>("text")
    }

    override fun serialize(
        encoder: Encoder,
        value: PartObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): PartObjc {
        var text = ""
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> text = decodeStringElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        return TextPartObjc(text)
    }
}

object CitationObjcSerializer: KSerializer<CitationObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CitationObjc") {
        element<Long>("startIndex")
        element<Long>("endIndex")
        element<String>("uri")
        element<String>("title")
        element<String>("license")
        element("publicationDate", PublicationDateSerializer.descriptor)
    }

    override fun serialize(
        encoder: Encoder,
        value: CitationObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): CitationObjc {
        var startIndex = 0L
        var endIndex = 0L
        var uri: String? = null
        var title: String? = null
        var license: String? = null
        var publicationDate: PublicationDate? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> startIndex = decodeLongElement(descriptor, index)
                    1 -> endIndex = decodeLongElement(descriptor, index)
                    2 -> uri = decodeStringElement(descriptor, index)
                    3 -> title = decodeStringElement(descriptor, index)
                    4 -> license = decodeStringElement(descriptor, index)
                    5 -> publicationDate = decodeSerializableElement(descriptor, index, PublicationDateSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        return CitationObjc(
            startIndex = startIndex,
            endIndex = endIndex,
            uri = uri,
            title = title,
            license = license,
            publicationDate = publicationDate as NSDateComponents?
        )
    }
}

object PublicationDateSerializer: KSerializer<PublicationDate> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PublicationDate") {
        element<Long>("year")
        element<Long>("month")
        element<Long>("day")
    }

    override fun serialize(
        encoder: Encoder,
        value: PublicationDate
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): PublicationDate {
        var year = 0L
        var month = 0L
        var day = 0L

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> year = decodeLongElement(descriptor, index)
                    1 -> month = decodeLongElement(descriptor, index)
                    2 -> day = decodeLongElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }

        return PublicationDate(
            year = year,
            month = month,
            day = day
        )
    }
}

class PublicationDate(
    year: Long,
    month: Long,
    day: Long
)
