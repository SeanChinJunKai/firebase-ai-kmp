package io.github.seanchinjunkai.firebase.ai.type

public class FunctionDeclaration(
    internal val name: String,
    internal val description: String,
    internal val parameters: Map<String, Schema>,
    internal val optionalParameters: List<String> = emptyList(),
) {

}
