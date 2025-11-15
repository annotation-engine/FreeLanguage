package free.core.parser.expression.matcher

import free.core.FreeContext
import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression
import free.core.parser.expression.parser.PostfixExpressionParser

object PostfixExpressionMatcher : ExpressionMatcher<Expression> {
	
	private val tokenTypes = listOf(
		NUMBER, STRING, CHAR, TRUE, FALSE, IDENTIFIER,
		THIS, SUPER, NULL,
		DOUBLE_COLON
	)
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		tokenTypes.forEach {
			if (ctx.match(it)) return true
		}
		return false
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): Expression {
		return PostfixExpressionParser(ctx).parse()
	}
}