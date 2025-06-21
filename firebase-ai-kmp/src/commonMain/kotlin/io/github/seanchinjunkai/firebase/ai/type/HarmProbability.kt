package io.github.seanchinjunkai.firebase.ai.type

public class HarmProbability private constructor(public val ordinal: Int) {
    public companion object {
        public val UNKNOWN: HarmProbability = HarmProbability(0)

        public val NEGLIGIBLE: HarmProbability = HarmProbability(1)

        public val LOW: HarmProbability = HarmProbability(2)

        public val MEDIUM: HarmProbability = HarmProbability(3)

        public val HIGH: HarmProbability = HarmProbability(4)
    }
}