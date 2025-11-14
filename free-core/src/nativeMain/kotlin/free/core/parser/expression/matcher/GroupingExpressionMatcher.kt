package free.core.parser.expression.matcher

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression
import free.core.parser.expression.GroupingExpression
import free.core.parser.expression.parser.GroupingExpressionParser

object GroupingExpressionMatcher : ExpressionMatcher<GroupingExpression> {
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		return ctx.match(FreeTokenType.LPAREN)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): GroupingExpression {
		return GroupingExpressionParser(ctx).parse()
	}
}