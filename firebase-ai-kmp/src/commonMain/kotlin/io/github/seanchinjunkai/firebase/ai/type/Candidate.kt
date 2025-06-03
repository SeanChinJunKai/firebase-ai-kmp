package io.github.seanchinjunkai.firebase.ai.type

public class Candidate(
    public val content: Content,
    public val safetyRatings: List<SafetyRating>,
    public val citationMetadata: CitationMetadata?,
    public val finishReason: FinishReason?
) {

}
