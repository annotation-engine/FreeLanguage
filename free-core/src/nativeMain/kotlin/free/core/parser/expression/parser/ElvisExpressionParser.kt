package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.parser.FreeParserContext
import free.core.parser.expression.ElvisExpression
import free.core.parser.expression.Expression
import free.core.parser.expression.matcher.parseCompleteExpression

class ElvisExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(left: Expression): ElvisExpression {
		val right = parseCompleteExpression(ctx)
		return ElvisExpression(left, right)
	}
}