package free.core.parser.expression

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext

class PrimaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): Expression {
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