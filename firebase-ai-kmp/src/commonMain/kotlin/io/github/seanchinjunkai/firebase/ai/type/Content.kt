package io.github.seanchinjunkai.firebase.ai.type

import io.github.seanchinjunkai.firebase.ai.type.PlatformImage

public class Content(
    public val role: String? = "user",
    public val parts: List<Part>
) {

    /** Returns a copy of this object, with the provided parameters overwriting the originals. */
    public fun copy(role: String? = this.role, parts: List<Part> = this.parts): Content {
        return Content(role, parts)
    }

    public class Builder {

        public var role: String? = "user"

        public var parts: MutableList<Part> = arrayListOf()

        public fun setRole(role: String?): Content.Builder = apply { this.role = role }
        public fun setParts(parts: MutableList<Part>): Content.Builder = apply { this.parts = parts }

        public fun <T : Part> part(data: T): Content.Builder = apply { parts.add(data) }

        public fun text(text: String): Content.Builder = part(TextPart(text))

        public fun inlineData(bytes: ByteArray, mimeType: String): Content.Builder =
            part(InlineDataPart(bytes, mimeType))

        public fun image(image: PlatformImage): Content.Builder = part(ImagePart(image))

        public fun fileData(uri: String, mimeType: String): Content.Builder =
            part(FileDataPart(uri, mimeType))

        public fun build(): Content = Content(role, parts)
    }
}

public fun content(role: String? = "user", init: Content.Builder.() -> Unit): Content {
    val builder = Content.Builder()
    builder.role = role
    builder.init()
    return builder.build()
}