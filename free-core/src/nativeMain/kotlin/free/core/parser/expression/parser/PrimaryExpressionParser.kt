package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext
import free.core.parser.expression.*

class PrimaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): Expression {
		val token = ctx.previous
		return when (token.type) {
			NUMBER -> NumberLiteral(token.value)
			STRING -> StringLiteral(token.value)
			CHAR -> CharLiteral(token.value)
			TRUE -> BooleanLiteral(true)
			FALSE -> BooleanLiteral(false)
			IDENTIFIER -> IdentifierExpression(token.value)
			else -> syntaxError("不支持的基础表达式", token)
		}
	}
}