package free.core.parser.expression

import free.core.lexer.FreeTokenType
import kotlinx.serialization.Serializable

sealed interface InvokeExpression : Expression {
	
	val callee: Expression
	
	val arguments: List<Argument>
}

@Serializable
data class CallExpression(
	override val callee: Expression,
	override val arguments: List<Argument> = emptyList(),
) : InvokeExpression

@Serializable
data class IndexAccessExpression(
	override val callee: Expression,
	override val arguments: List<Argument> = emptyList(),
) : InvokeExpression

@Serializable
data class Argument(
	val name: String?,
	val expression: Expression,
)

enum class InvokeType(
	val startTokenType: FreeTokenType,
	val endTokenType: FreeTokenType,
) {
	CALL(FreeTokenType.LPAREN, FreeTokenType.RPAREN),
	INDEX_ACCESS(FreeTokenType.LBRACKET, FreeTokenType.RBRACKET),
}