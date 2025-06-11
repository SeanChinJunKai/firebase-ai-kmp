package io.github.seanchinjunkai.firebase.ai.type

public abstract class StringFormat private constructor(internal val value: String) {
    public class Custom(value: String) : StringFormat(value)
}

public class Schema
internal constructor(
    public val type: String,
    public val description: String? = null,
    public val format: String? = null,
    public val nullable: Boolean? = null,
    public val enum: List<String>? = null,
    public val properties: Map<String, Schema>? = null,
    public val required: List<String>? = null,
    public val items: Schema? = null,
    public val title: String? = null,
    public val minItems: Int? = null,
    public val maxItems: Int? = null,
    public val minimum: Double? = null,
    public val maximum: Double? = null,
    public val anyOf: List<Schema>? = null,
) {

    public companion object {
        public fun boolean(
            description: String? = null,
            nullable: Boolean = false,
            title: String? = null,
        ): Schema =
            Schema(description = description, nullable = nullable, type = "BOOLEAN", title = title)

        public fun integer(
            description: String? = null,
            nullable: Boolean = false,
            title: String? = null,
            minimum: Double? = null,
            maximum: Double? = null,
        ): Schema =
            Schema(
                description = description,
                format = "int32",
                nullable = nullable,
                type = "INTEGER",
                title = title,
                minimum = minimum,
                maximum = maximum,
            )

        public fun long(
            description: String? = null,
            nullable: Boolean = false,
            title: String? = null,
            minimum: Double? = null,
            maximum: Double? = null,
        ): Schema =
            Schema(
                description = description,
                nullable = nullable,
                type = "INTEGER",
                title = title,
                minimum = minimum,
                maximum = maximum,
            )

        public fun double(
            description: String? = null,
            nullable: Boolean = false,
            title: String? = null,
            minimum: Double? = null,
            maximum: Double? = null,
        ): Schema =
            Schema(
                description = description,
                nullable = nullable,
                type = "NUMBER",
                title = title,
                minimum = minimum,
                maximum = maximum,
            )

        public fun float(
            description: String? = null,
            nullable: Boolean = false,
            title: String? = null,
            minimum: Double? = null,
            maximum: Double? = null,
        ): Schema =
            Schema(
                description = description,
                nullable = nullable,
                type = "NUMBER",
                format = "float",
                title = title,
                minimum = minimum,
                maximum = maximum,
            )

        public fun string(
            description: String? = null,
            nullable: Boolean = false,
            format: StringFormat? = null,
            title: String? = null,
        ): Schema =
            Schema(
                description = description,
                format = format?.value,
                nullable = nullable,
                type = "STRING",
                title = title,
            )

        public fun obj(
            properties: Map<String, Schema>,
            optionalProperties: List<String> = emptyList(),
            description: String? = null,
            nullable: Boolean = false,
            title: String? = null,
        ): Schema {
            if (!properties.keys.containsAll(optionalProperties)) {
                throw IllegalArgumentException(
                    "All optional properties must be present in properties. Missing: ${optionalProperties.minus(properties.keys)}"
                )
            }
            return Schema(
                description = description,
                nullable = nullable,
                properties = properties,
                required = properties.keys.minus(optionalProperties.toSet()).toList(),
                type = "OBJECT",
                title = title,
            )
        }

        public fun array(
            items: Schema,
            description: String? = null,
            nullable: Boolean = false,
            title: String? = null,
            minItems: Int? = null,
            maxItems: Int? = null,
        ): Schema =
            Schema(
                description = description,
                nullable = nullable,
                items = items,
                type = "ARRAY",
                title = title,
                minItems = minItems,
                maxItems = maxItems,
            )

        public fun enumeration(
            values: List<String>,
            description: String? = null,
            nullable: Boolean = false,
            title: String? = null,
        ): Schema =
            Schema(
                description = description,
                format = "enum",
                nullable = nullable,
                enum = values,
                type = "STRING",
                title = title,
            )

        public fun anyOf(schemas: List<Schema>): Schema = Schema(type = "ANYOF", anyOf = schemas)
    }
}
