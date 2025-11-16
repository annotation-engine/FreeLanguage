package free.core.parser.expression.matcher

import free.core.FreeContext
import free.core.lexer.FreeTokenType.DOUBLE_MINUS
import free.core.lexer.FreeTokenType.DOUBLE_PLUS
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression
import free.core.parser.expression.SuffixUnaryExpression
import free.core.parser.expression.parser.SuffixUnaryExpressionParser

object SuffixUnaryExpressionMatcher : ExpressionMatcher<SuffixUnaryExpression> {
	
	private val tokenTypes = listOf(DOUBLE_PLUS, DOUBLE_MINUS)
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		return ctx.next.type in tokenTypes
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): SuffixUnaryExpression {
		return SuffixUnaryExpressionParser(ctx).parse()
	}
}