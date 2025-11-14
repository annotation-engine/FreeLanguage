package free.core.parser.expression

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Operator
import free.core.parser.toOperator
import kotlinx.serialization.Serializable

@Serializable
data class SuffixUnaryExpression(
	val expression: Expression,
	val operator: Operator
) : Expression

class SuffixUnaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): SuffixUnaryExpression {
		ctx.expect(FreeTokenType.IDENTIFIER, "一元运算符前必须跟标识符")
		val expression = IdentifierExpression(ctx.previous.value)
		ctx.advance()
		val operator = ctx.previous.type.toOperator()
		return SuffixUnaryExpression(expression, operator)
	}
}