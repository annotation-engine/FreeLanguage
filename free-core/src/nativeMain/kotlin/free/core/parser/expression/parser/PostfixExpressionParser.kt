package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.*

class PostfixExpressionParser(
	private val ctx: FreeParserContext
) {
	private val accessTokenTypes = listOf(
		FreeTokenType.DOT,
		FreeTokenType.QUESTION_DOT,
		FreeTokenType.DOUBLE_COLON
	)
	
	context(_: FreeContext)
	fun parse(): Expression {
		var receiver = if (ctx.previous.type == FreeTokenType.DOUBLE_COLON) {
			ctx.expect(FreeTokenType.IDENTIFIER, "访问操作符后必须跟标识符")
			val name = ctx.previous.value
			PropertyAccessExpression(
				receiver = ThisLiteral,
				operator = AccessOperator.DOUBLE_COLON,
				expression = IdentifierExpression(name)
			)
		} else getPrimaryExpression()
		if (ctx.match(FreeTokenType.BANG)) {
			receiver = NonNullAssertionExpression(receiver)
		}
		while (isAccessOperator()) {
			val operator = ctx.previous.type.toAccessOperator()
			ctx.expect(FreeTokenType.IDENTIFIER, "访问操作符后必须跟标识符")
			var expression: Expression = IdentifierExpression(ctx.previous.value)
			if (ctx.match(FreeTokenType.BANG)) {
				expression = NonNullAssertionExpression(expression)
			}
			receiver = PropertyAccessExpression(receiver, operator, expression)
		}
		return receiver
	}
	
	context(_: FreeContext)
	private fun getPrimaryExpression(): Expression {
		val token = ctx.previous
		return when (token.type) {
			FreeTokenType.NUMBER -> NumberLiteral(token.value)
			FreeTokenType.STRING -> StringLiteral(token.value)
			FreeTokenType.CHAR -> CharLiteral(token.value)
			FreeTokenType.TRUE -> BooleanLiteral(true)
			FreeTokenType.FALSE -> BooleanLiteral(false)
			FreeTokenType.THIS -> ThisLiteral
			FreeTokenType.SUPER -> SuperLiteral
			FreeTokenType.NULL -> NullLiteral
			FreeTokenType.IDENTIFIER -> IdentifierExpression(token.value)
			else -> syntaxError("不支持的基础表达式", token)
		}
	}
	
	private fun isAccessOperator(): Boolean {
		accessTokenTypes.forEach {
			if (ctx.match(it)) return true
		}
		return false
	}
}