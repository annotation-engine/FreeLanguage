package free.core.parser.expression.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression

sealed interface ExpressionMatcher<out E : Expression> {
	
	fun match(ctx: FreeParserContext, left: Expression?): Boolean
	
	context(_: FreeContext)
	fun parse(ctx: FreeParserContext, left: Expression?): E
}

private val matchers = listOf(
	GroupingExpressionMatcher,
	PrefixUnaryExpressionMatcher,
	SuffixUnaryExpressionMatcher,
	BinaryExpressionMatcher,
	TernaryExpressionMatcher,
	PostfixExpressionMatcher,
)

context(_: FreeContext)
fun parseExpression(ctx: FreeParserContext, left: Expression? = null): Expression {
	matchers.forEach {
		if (it.match(ctx, left)) {
			return it.parse(ctx, left)
		}
	}
	syntaxError("不支持的表达式", ctx.current)
}