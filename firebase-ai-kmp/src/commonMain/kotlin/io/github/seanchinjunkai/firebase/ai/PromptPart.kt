package io.github.seanchinjunkai.firebase.ai

sealed class PromptPart{
    data class TextPart(public val text: String): PromptPart()
    data class FileDataPart(public val uri: String, public val mimeType: String): PromptPart()
    data class InlineDataPart(public val inlineData: ByteArray, public val mimeType: String): PromptPart()
    data class ImagePart(public val image: PlatformImage): PromptPart()
}



