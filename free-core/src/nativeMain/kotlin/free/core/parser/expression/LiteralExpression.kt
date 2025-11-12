package free.core.parser.expression

import kotlinx.serialization.Serializable

@Serializable
data class NumberLiteral(val value: String) : Expression

@Serializable
data class StringLiteral(val value: String) : Expression

@Serializable
data class BooleanLiteral(val value: Boolean) : Expression

@Serializable
data class CharLiteral(val value: String) : Expression