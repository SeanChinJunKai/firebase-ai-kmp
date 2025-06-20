package io.github.seanchinjunkai.firebase.ai.type

public class FinishReason private constructor(public val name: String, public val ordinal: Int) {

    public companion object {
        public val UNKNOWN:FinishReason = FinishReason("UNKNOWN", 0)

        public val STOP: FinishReason = FinishReason("STOP", 1)

        public val MAX_TOKENS: FinishReason = FinishReason("MAX_TOKENS", 2)

        public val SAFETY: FinishReason = FinishReason("SAFETY", 3)

        public val RECITATION: FinishReason = FinishReason("RECITATION", 4)

        public val OTHER: FinishReason = FinishReason("OTHER", 5)

        public val BLOCKLIST: FinishReason = FinishReason("BLOCKLIST", 6)

        public val PROHIBITED_CONTENT: FinishReason = FinishReason("PROHIBITED_CONTENT", 7)

        public val SPII: FinishReason = FinishReason("SPII", 8)

        public val MALFORMED_FUNCTION_CALL: FinishReason = FinishReason("MALFORMED_FUNCTION_CALL", 9)
    }
}