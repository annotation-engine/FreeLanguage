package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.IdentifierExpression
import free.core.parser.expression.PrefixUnaryExpression
import free.core.parser.toOperator

class PrefixUnaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): PrefixUnaryExpression {
		val operator = ctx.previous.type.toOperator()
		ctx.expect(FreeTokenType.IDENTIFIER, "一元运算符后只允许跟标识符")
		val expression = IdentifierExpression(ctx.previous.value)
		return PrefixUnaryExpression(operator, expression)
	}
}