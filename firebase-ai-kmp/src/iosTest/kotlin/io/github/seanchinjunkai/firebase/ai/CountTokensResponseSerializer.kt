@file:OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import cocoapods.FirebaseAIBridge.ContentModalityObjc
import cocoapods.FirebaseAIBridge.CountTokensResponseObjc
import cocoapods.FirebaseAIBridge.ModalityTokenCountObjc
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
import platform.Foundation.NSNumber
import kotlin.experimental.ExperimentalNativeApi

object CountTokensResponseObjcSerializer : KSerializer<CountTokensResponseObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CountTokensResponseObjc") {
        element<Long>("totalTokens")
        element<Long?>("totalBillableCharacters")
        element("promptTokensDetails", ListSerializer(ModalityTokenCountObjcSerializer).descriptor)
    }

    override fun serialize(
        encoder: Encoder,
        value: CountTokensResponseObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): CountTokensResponseObjc {
        var totalTokens = 0L
        var totalBillableCharacters: Long? = null
        var promptTokenDetails: List<ModalityTokenCountObjc> = emptyList()

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> totalTokens = decodeLongElement(descriptor, index)
                    1 -> totalBillableCharacters = decodeLongElement(descriptor, index)
                    2 -> promptTokenDetails = decodeSerializableElement(
                        descriptor,
                        index,
                        ListSerializer(ModalityTokenCountObjcSerializer)
                    )
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }

        val nsNumberBillable = totalBillableCharacters?.let { NSNumber(long = it) }
        return CountTokensResponseObjc(totalTokens, nsNumberBillable, promptTokenDetails)
    }
}

object ModalityTokenCountObjcSerializer : KSerializer<ModalityTokenCountObjc> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ModalityTokenCountObjc") {
        element<String>("modality")
        element<Long>("tokenCount")
    }

    override fun serialize(
        encoder: Encoder,
        value: ModalityTokenCountObjc
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): ModalityTokenCountObjc {
        var modality = ""
        var tokenCount = 0L

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> modality = decodeStringElement(descriptor, index)
                    1 -> tokenCount = decodeLongElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        return ModalityTokenCountObjc(ContentModalityObjc(modality), tokenCount)
    }
}