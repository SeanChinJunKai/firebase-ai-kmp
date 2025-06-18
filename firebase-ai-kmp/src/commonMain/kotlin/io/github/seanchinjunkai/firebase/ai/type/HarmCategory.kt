package io.github.seanchinjunkai.firebase.ai.type

public class HarmCategory private constructor(public val ordinal: Int) {
    public companion object {
        public val UNKNOWN: HarmCategory = HarmCategory(0)

        public val HARASSMENT: HarmCategory = HarmCategory(1)

        public val HATE_SPEECH: HarmCategory = HarmCategory(2)

        public val SEXUALLY_EXPLICIT: HarmCategory = HarmCategory(3)

        public val DANGEROUS_CONTENT: HarmCategory = HarmCategory(4)

        public val CIVIC_INTEGRITY: HarmCategory = HarmCategory(5)
    }
}
