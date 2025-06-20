package io.github.seanchinjunkai.firebase.ai.type

public class ContentModality private constructor(public val ordinal: Int) {
    public companion object {
        public val UNSPECIFIED: ContentModality = ContentModality(0)

        public val TEXT: ContentModality = ContentModality(1)

        public val IMAGE: ContentModality = ContentModality(2)

        public val VIDEO: ContentModality = ContentModality(3)

        public val AUDIO: ContentModality = ContentModality(4)

        public val DOCUMENT: ContentModality = ContentModality(5)
    }
}
