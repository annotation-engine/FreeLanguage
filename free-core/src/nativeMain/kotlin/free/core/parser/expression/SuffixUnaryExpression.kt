package free.core.parser.expression

import free.core.parser.FreeParserContext
import kotlinx.serialization.Serializable

@Serializable
data class SuffixUnaryExpression(
	val expression: Expression,
	val operator: Operator
) : Expression

class SuffixUnaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): SuffixUnaryExpression {
		val operator = ctx.next.type.toOperator()
		val expression = PrimaryExpressionParser(ctx).parse(isUnary = true)
		ctx.advance()
		return SuffixUnaryExpression(expression, operator)
	}
}