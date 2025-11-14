package free.core.parser.expression

import free.core.FreeContext
import free.core.lexer.FreeTokenType.RPAREN
import free.core.parser.FreeParserContext
import free.core.parser.matcher.ExpressionMatcher
import kotlinx.serialization.Serializable

/**
 * 括号表达式
 */
@Serializable
data class GroupingExpression(
	val expression: Expression
) : Expression

class GroupingExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): GroupingExpression {
		var expression: Expression? = null
		do {
			expression = ExpressionMatcher.parse(ctx, expression)
		} while (!ctx.match(RPAREN))
		return GroupingExpression(expression)
	}
}