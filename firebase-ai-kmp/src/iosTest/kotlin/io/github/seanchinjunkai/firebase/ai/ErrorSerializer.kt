@file:OptIn(ExperimentalForeignApi::class)

package io.github.seanchinjunkai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import platform.Foundation.NSError


object NSErrorSerializer: KSerializer<NSError> {
    private val userInfoSerializer = MapSerializer<String, String>(String.serializer(), String.serializer())

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CountTokensResponseObjc") {
        element<String>("domain")
        element<Long>("code")
        element("userInfo", userInfoSerializer.descriptor)
    }

    override fun serialize(
        encoder: Encoder,
        value: NSError
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): NSError {
        var domain: String = ""
        var code: Long = 0L
        var userInfo: Map<String, String> = emptyMap()

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> domain = decodeStringElement(descriptor, 0)
                    1 -> code = decodeLongElement(descriptor, 1)
                    2 -> userInfo = decodeSerializableElement(
                        descriptor,
                        index,
                        userInfoSerializer
                    )
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        val userInfoDict = userInfo.mapValues { (key, value) ->
            if (key == "response") {
                json.decodeFromString(GenerateContentResponseObjcSerializer, value)
            } else {
                value
            }
        }

        return NSError(
            domain = domain,
            code = code,
            userInfo = userInfoDict as Map<Any?, *>?
        )
    }
}