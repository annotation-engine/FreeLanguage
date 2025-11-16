package free.core.parser.expression.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.ElvisExpression
import free.core.parser.expression.Expression
import free.core.parser.expression.parser.ElvisExpressionParser

object ElvisExpressionMatcher : ExpressionMatcher<ElvisExpression> {
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		return ctx.match(FreeTokenType.ELVIS)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): ElvisExpression {
		if (left == null) {
			syntaxError("'?:' 操作符前必须跟表达式", ctx.previous)
		}
		return ElvisExpressionParser(ctx).parse(left)
	}
	
}