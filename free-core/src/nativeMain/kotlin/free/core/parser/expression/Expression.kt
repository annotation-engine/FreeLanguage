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
 * 属性访问
 */
@Serializable
data class PropertyAccessExpression(
	val receiver: Expression?,
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

/**
 * 三元表达式
 */
@Serializable
data class TernaryExpression(
	val condition: Expression,
	val thenExpression: Expression,
	val elseExpression: Expression
) : Expression

/**
 * 括号表达式
 */
@Serializable
data class GroupingExpression(
	val expression: Expression
) : Expression