package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType.RPAREN
import free.core.parser.FreeParserContext
import free.core.parser.expression.*
import free.core.parser.expression.matcher.parseCompleteExpression

class GroupingExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): Expression {
		var expression = parseCompleteExpression(ctx)
		if (ctx.current.type != RPAREN) {
			syntaxError("'(' 必须由 ')' 结束", ctx.current)
		}
		ctx.advance()
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