package io.github.seanchinjunkai.firebase.ai.type

public class Citation(
    public val title: String? = null,
    public val startIndex: Int = 0,
    public val endIndex: Int,
    public val uri: String? = null,
    public val license: String? = null,
    public val publicationDate: Date?
) {

}