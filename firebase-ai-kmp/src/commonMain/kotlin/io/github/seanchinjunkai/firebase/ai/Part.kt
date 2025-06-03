package io.github.seanchinjunkai.firebase.ai

public interface Part {}

public class TextPart(public val text: String) : Part

public class ImagePart(public val image: PlatformImage) : Part

public class InlineDataPart(public val inlineData: ByteArray, public val mimeType: String) : Part

public class FileDataPart(public val uri: String, public val mimeType: String) : Part

