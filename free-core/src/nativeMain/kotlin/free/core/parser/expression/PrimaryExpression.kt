package free.core.parser.expression

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext

class PrimaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(isUnary: Boolean = false): Expression {
		val token = ctx.previous
		return when (token.type) {
			NUMBER -> NumberLiteral(token.value)
			STRING -> if (isUnary) syntaxError("字符串不支持一元表达式", ctx.current) else StringLiteral(token.value)
			CHAR -> if (isUnary) syntaxError("字符不支持一元表达式", ctx.current) else CharLiteral(token.value)
			TRUE -> if (isUnary) syntaxError("Boolean 不支持一元表达式", ctx.current) else BooleanLiteral(true)
			FALSE -> if (isUnary) syntaxError("Boolean 不支持一元表达式", ctx.current) else BooleanLiteral(false)
			IDENTIFIER -> IdentifierExpression(token.value)
			else -> syntaxError("不支持的基础表达式", ctx.current)
		}
	}
}