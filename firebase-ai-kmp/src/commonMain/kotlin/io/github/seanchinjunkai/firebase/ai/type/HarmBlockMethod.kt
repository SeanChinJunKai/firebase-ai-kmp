package io.github.seanchinjunkai.firebase.ai.type

public class HarmBlockMethod private constructor(public val ordinal: Int) {
    public companion object {
        public val SEVERITY: HarmBlockMethod = HarmBlockMethod(0)

        public val PROBABILITY: HarmBlockMethod = HarmBlockMethod(1)
    }
}