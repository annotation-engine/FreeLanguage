package free.core.parser.expression

import free.core.parser.FreeParserContext
import kotlinx.serialization.Serializable

@Serializable
data class PrefixUnaryExpression(
	val operator: Operator,
	val expression: Expression
) : Expression

class PrefixUnaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): PrefixUnaryExpression {
		val operator = ctx.previous.type.toOperator()
		val expression = PrimaryExpressionParser(ctx).parse(isUnary = true)
		return PrefixUnaryExpression(operator, expression)
	}
}