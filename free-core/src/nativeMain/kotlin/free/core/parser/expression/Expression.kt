package free.core.parser.expression

import kotlinx.serialization.Serializable

@Serializable
sealed interface Expression

/**
 * 标识符
 */
@Serializable
data class IdentifierExpression(
	val name: String
) : Expression

/**
 * 函数调用
 */
@Serializable
data class CallExpression(
	val callee: Expression,
	val arguments: List<Expression> = emptyList()
) : Expression