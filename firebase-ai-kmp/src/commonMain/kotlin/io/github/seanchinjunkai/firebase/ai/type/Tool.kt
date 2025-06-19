package io.github.seanchinjunkai.firebase.ai.type

public class Tool
internal constructor(internal val functionDeclarations: List<FunctionDeclaration>?) {
    public companion object {
        public fun functionDeclarations(functionDeclarations: List<FunctionDeclaration>): Tool {
            return Tool(functionDeclarations)
        }
    }
}