package free.core.parser.expression

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.matcher.ExpressionMatcher
import free.core.parser.statement.isStatementEnd
import kotlinx.serialization.Serializable

/**
 * 三元表达式
 */
@Serializable
data class TernaryExpression(
	val condition: Expression,
	val thenExpression: Expression,
	val elseExpression: Expression
) : Expression

class TernaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(condition: Expression): TernaryExpression {
		var thenExpression: Expression? = null
		do {
			thenExpression = ExpressionMatcher.parse(ctx, thenExpression)
			println(ctx.current.type == FreeTokenType.COLON)
		} while (!ctx.match(FreeTokenType.COLON))
		var elseExpression: Expression? = null
		do {
			elseExpression = ExpressionMatcher.parse(ctx, elseExpression)
		} while (!isStatementEnd(ctx) && ctx.current.type != FreeTokenType.COLON)
		return TernaryExpression(condition, thenExpression, elseExpression)
	}
}