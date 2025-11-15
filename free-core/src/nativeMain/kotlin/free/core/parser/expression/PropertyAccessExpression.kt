package free.core.parser.expression

import free.core.lexer.FreeTokenType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertyAccessExpression(
	val receiver: Expression,
	val operator: AccessOperator,
	val expression: Expression
) : Expression

@Serializable
enum class AccessOperator {
	
	@SerialName(".")
	DOT,
	
	@SerialName("?.")
	QUESTION_DOT,
	
	@SerialName("!.")
	NON_NULL_ACCESS,
	
	@SerialName("::")
	DOUBLE_COLON
}

fun FreeTokenType.toAccessOperator(): AccessOperator = when (this) {
	FreeTokenType.DOT -> AccessOperator.DOT
	FreeTokenType.QUESTION_DOT -> AccessOperator.QUESTION_DOT
	FreeTokenType.DOUBLE_COLON -> AccessOperator.DOUBLE_COLON
	else -> error("不支持的操作符")
}