package io.github.seanchinjunkai.firebase.ai.type

public class HarmSeverity private constructor(public val ordinal: Int) {
    public companion object {
        public val UNKNOWN: HarmSeverity = HarmSeverity(0)

        public val NEGLIGIBLE: HarmSeverity = HarmSeverity(1)

        public val LOW: HarmSeverity = HarmSeverity(2)

        public val MEDIUM: HarmSeverity = HarmSeverity(3)

        public val HIGH: HarmSeverity = HarmSeverity(4)
    }
}
