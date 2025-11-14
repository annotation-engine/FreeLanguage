package free.core.parser.expression.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression
import free.core.parser.expression.TernaryExpression
import free.core.parser.expression.parser.TernaryExpressionParser

object TernaryExpressionMatcher : ExpressionMatcher<TernaryExpression> {
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		return ctx.match(FreeTokenType.QUESTION)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): TernaryExpression {
		if (left == null) {
			syntaxError("三元运算符 '?' 前未解析到表达式", ctx.peek(offset = -2)!!)
		}
		return TernaryExpressionParser(ctx).parse(left)
	}
}