package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.IdentifierExpression
import free.core.parser.expression.SuffixUnaryExpression
import free.core.parser.toOperator

class SuffixUnaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): SuffixUnaryExpression {
		ctx.expect(FreeTokenType.IDENTIFIER, "一元运算符前必须跟标识符")
		val expression = IdentifierExpression(ctx.previous.value)
		ctx.advance()
		val operator = ctx.previous.type.toOperator()
		return SuffixUnaryExpression(expression, operator)
	}
}