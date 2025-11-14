package free.core.parser.expression

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Operator
import free.core.parser.toOperator
import kotlinx.serialization.Serializable

@Serializable
data class PrefixUnaryExpression(
	val operator: Operator,
	val expression: Expression
) : Expression

class PrefixUnaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): PrefixUnaryExpression {
		val operator = ctx.previous.type.toOperator()
		ctx.expect(FreeTokenType.IDENTIFIER, "一元运算符后只允许跟标识符")
		val expression = IdentifierExpression(ctx.previous.value)
		return PrefixUnaryExpression(operator, expression)
	}
}