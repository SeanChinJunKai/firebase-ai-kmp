package io.github.seanchinjunkai.firebase.ai.type

public class ResponseModality private constructor(public val ordinal: Int) {
    public companion object {
        public val TEXT: ResponseModality = ResponseModality(1)

        public val IMAGE: ResponseModality = ResponseModality(2)

        public val AUDIO: ResponseModality = ResponseModality(4)
    }
}