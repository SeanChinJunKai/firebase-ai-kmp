package io.github.seanchinjunkai.firebase.ai.type

public class BlockReason private constructor(public val name: String, public val ordinal: Int) {
    public companion object {
        public val UNKNOWN: BlockReason = BlockReason("UNKNOWN", 0)

        public val SAFETY: BlockReason = BlockReason("SAFETY", 1)

        public val OTHER: BlockReason = BlockReason("OTHER", 2)

        public val BLOCKLIST: BlockReason = BlockReason("BLOCKLIST", 3)

        public val PROHIBITED_CONTENT: BlockReason = BlockReason("PROHIBITED_CONTENT", 4)
    }
}