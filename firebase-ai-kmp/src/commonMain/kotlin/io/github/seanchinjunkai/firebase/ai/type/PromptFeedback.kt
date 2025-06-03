package io.github.seanchinjunkai.firebase.ai.type

public class PromptFeedback(
    public val blockReason: BlockReason?,
    public val safetyRatings: List<SafetyRating>,
    public val blockReasonMessage: String?
) {

}