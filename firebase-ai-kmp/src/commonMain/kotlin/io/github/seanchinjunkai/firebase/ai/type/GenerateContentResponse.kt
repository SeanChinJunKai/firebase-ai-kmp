package io.github.seanchinjunkai.firebase.ai.type

// TODO: Think about how to include text
public class GenerateContentResponse(
    public val candidates: List<Candidate>,
    public val promptFeedback: PromptFeedback?,
    public val usageMetadata: UsageMetadata?,
) {
    public val text: String? by lazy {
        candidates.first().content.parts.filterIsInstance<TextPart>().joinToString(" ") { it.text }
    }
}

