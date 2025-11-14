package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression
import free.core.parser.expression.TernaryExpression
import free.core.parser.expression.matcher.parseExpression
import free.core.parser.statement.isStatementEnd

class TernaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(condition: Expression): TernaryExpression {
		var thenExpression: Expression? = null
		do {
			thenExpression = parseExpression(ctx, thenExpression)
			println(ctx.current.type == FreeTokenType.COLON)
		} while (!ctx.match(FreeTokenType.COLON))
		var elseExpression: Expression? = null
		do {
			elseExpression = parseExpression(ctx, elseExpression)
		} while (!isStatementEnd(ctx) && ctx.current.type != FreeTokenType.COLON)
		return TernaryExpression(condition, thenExpression, elseExpression)
	}
}