package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType.COLON
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression
import free.core.parser.expression.TernaryExpression
import free.core.parser.expression.matcher.parseCompleteExpression

class TernaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(condition: Expression): TernaryExpression {
		val thenExpression = parseCompleteExpression(ctx)
		if (ctx.current.type != COLON) {
			syntaxError("三元运算符缺少 ':'", ctx.current)
		}
		ctx.advance()
		val elseExpression = parseCompleteExpression(ctx)
		return TernaryExpression(condition, thenExpression, elseExpression)
	}
}