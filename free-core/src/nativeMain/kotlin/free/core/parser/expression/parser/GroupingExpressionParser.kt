package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.*
import free.core.parser.expression.matcher.parseExpression

class GroupingExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): Expression {
		var expression: Expression? = null
		do {
			expression = parseExpression(ctx, expression)
		} while (!ctx.match(FreeTokenType.RPAREN))
		expression = if (isOmissible(expression)) expression else GroupingExpression(expression)
		return if (isAccessOperator(ctx)) {
			PostfixExpressionParser(ctx).parse(expression)
		} else expression
	}
	
	private fun isOmissible(expression: Expression): Boolean {
		return expression !is BinaryExpression
				&& expression !is PrefixUnaryExpression
				&& expression !is SuffixUnaryExpression
				&& expression !is TernaryExpression
	}
}