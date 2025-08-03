package io.github.seanchinjunkai.firebase.ai.type


public class ThinkingConfig
private constructor(
    internal val thinkingBudget: Int? = null,
) {
    public class Builder() {
        public var thinkingBudget: Int? = null

        public fun setThinkingBudget(thinkingBudget: Int): Builder = apply {
            this.thinkingBudget = thinkingBudget
        }

        public fun build(): ThinkingConfig = ThinkingConfig(thinkingBudget = thinkingBudget)
    }
}

public fun thinkingConfig(init: ThinkingConfig.Builder.() -> Unit): ThinkingConfig {
    val builder = ThinkingConfig.Builder()
    builder.init()
    return builder.build()
}
