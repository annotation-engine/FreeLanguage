package free.core.parser.expression.matcher

import free.core.FreeContext
import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression
import free.core.parser.expression.PrefixUnaryExpression
import free.core.parser.expression.parser.PrefixUnaryExpressionParser

object PrefixUnaryExpressionMatcher : ExpressionMatcher<PrefixUnaryExpression> {
	
	private val tokenTypes = listOf(PLUS, MINUS, BANG, BIT_NOT, DOUBLE_PLUS, DOUBLE_MINUS)
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		val type = ctx.current.type
		if (type !in tokenTypes) return false
		return if (left == null || type != PLUS && type != MINUS) {
			ctx.advance()
			true
		} else false
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): PrefixUnaryExpression {
		return PrefixUnaryExpressionParser(ctx).parse()
	}
}