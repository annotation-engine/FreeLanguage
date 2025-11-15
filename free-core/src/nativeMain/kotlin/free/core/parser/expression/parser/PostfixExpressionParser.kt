package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext
import free.core.parser.expression.*

class PostfixExpressionParser(
	private val ctx: FreeParserContext
) {
	context(_: FreeContext)
	fun parse(left: Expression?): Expression {
		var receiver = if (ctx.previous.type in accessTokenTypes) {
			val operator = ctx.previous.type.toAccessOperator()
			if (left == null && operator != AccessOperator.DOUBLE_COLON) {
				syntaxError("'.' 和 '?.' 访问操作符前必须跟标识符或字面量", ctx.previous)
			}
			val receiver = left ?: ThisLiteral
			ctx.expect(IDENTIFIER, "访问操作符后必须跟标识符")
			val name = ctx.previous.value
			PropertyAccessExpression(
				receiver = receiver,
				operator = operator,
				expression = IdentifierExpression(name)
			)
		} else getPrimaryExpression()
		if (ctx.match(BANG)) {
			receiver = NonNullAssertionExpression(receiver)
		}
		while (isAccessOperator(ctx)) {
			val operator = ctx.previous.type.toAccessOperator()
			ctx.expect(IDENTIFIER, "访问操作符后必须跟标识符")
			var expression: Expression = IdentifierExpression(ctx.previous.value)
			if (ctx.match(BANG)) {
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
			NUMBER -> NumberLiteral(token.value)
			STRING -> StringLiteral(token.value)
			CHAR -> CharLiteral(token.value)
			TRUE -> BooleanLiteral(true)
			FALSE -> BooleanLiteral(false)
			THIS -> ThisLiteral
			SUPER -> SuperLiteral
			NULL -> NullLiteral
			IDENTIFIER -> IdentifierExpression(token.value)
			else -> syntaxError("不支持的基础表达式", token)
		}
	}
}

private val accessTokenTypes = listOf(
	DOT,
	QUESTION_DOT,
	DOUBLE_COLON
)

fun isAccessOperator(ctx: FreeParserContext): Boolean {
	accessTokenTypes.forEach {
		if (ctx.match(it)) return true
	}
	return false
}