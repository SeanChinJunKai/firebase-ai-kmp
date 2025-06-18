package io.github.seanchinjunkai.firebase.ai.type

public class HarmBlockThreshold private constructor(public val ordinal: Int) {
    public companion object {
        public val LOW_AND_ABOVE: HarmBlockThreshold = HarmBlockThreshold(0)

        public val MEDIUM_AND_ABOVE: HarmBlockThreshold = HarmBlockThreshold(1)

        public val ONLY_HIGH: HarmBlockThreshold = HarmBlockThreshold(2)

        public val NONE: HarmBlockThreshold = HarmBlockThreshold(3)

        public val OFF: HarmBlockThreshold = HarmBlockThreshold(4)
    }
}