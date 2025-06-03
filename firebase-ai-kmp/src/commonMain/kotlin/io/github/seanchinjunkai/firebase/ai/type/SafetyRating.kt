package io.github.seanchinjunkai.firebase.ai.type

public class SafetyRating(
    public val category: HarmCategory,
    public val probability: HarmProbability,
    public val probabilityScore: Float,
    public val blocked: Boolean?,
    public val severity: HarmSeverity?,
    public val severityScore: Float?
) {

}
