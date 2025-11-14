package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression
import free.core.parser.expression.GroupingExpression
import free.core.parser.expression.matcher.parseExpression

class GroupingExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): GroupingExpression {
		var expression: Expression? = null
		do {
			expression = parseExpression(ctx, expression)
		} while (!ctx.match(FreeTokenType.RPAREN))
		return GroupingExpression(expression)
	}
}