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
		var receiver = parseInitialExpression(left)
		receiver = parseExpression(receiver)
		while (isAccessOperator(ctx)) {
			val operator = ctx.previous.type.toAccessOperator()
			var expression = parseIdentifierExpression()
			expression = parseExpression(expression)
			receiver = PropertyAccessExpression(
				receiver = receiver,
				operator = operator,
				expression = expression
			)
		}
		return receiver
	}
	
	private fun isInvoke(): Boolean {
		return ctx.match(LPAREN) || ctx.match(LBRACKET)
	}
	
	context(_: FreeContext)
	private fun parseInvokeExpression(callee: Expression): InvokeExpression {
		return when (ctx.previous.type) {
			InvokeType.CALL.startTokenType -> {
				val arguments = parseArguments(ctx, InvokeType.CALL)
				CallExpression(callee, arguments)
			}
			
			InvokeType.INDEX_ACCESS.startTokenType -> {
				val arguments = parseArguments(ctx, InvokeType.INDEX_ACCESS)
				IndexAccessExpression(callee, arguments)
			}
			
			else -> error("不支持的调用符号")
		}
	}
	
	context(_: FreeContext)
	private fun parseIdentifierExpression(): Expression {
		ctx.expect(IDENTIFIER, "访问操作符后必须跟标识符")
		return IdentifierExpression(ctx.previous.value)
	}
	
	context(_: FreeContext)
	private fun parseInitialExpression(receiver: Expression?): Expression {
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
			in accessTokenTypes -> {
				ctx.retreat()
				when {
					receiver != null -> {
						if (receiver is PrefixUnaryExpression) {
							syntaxError("访问操作符前不可以使用前缀一元运算符", ctx.peek(offset = -2)!!)
						} else receiver
					}
					
					token.type == DOUBLE_COLON -> ThisLiteral
					else -> syntaxError("'.' 和 '?.' 访问操作符前缺少接收者", ctx.previous)
				}
			}
			
			else -> syntaxError("不支持的基础表达式", token)
		}
	}
	
	context(_: FreeContext)
	private fun parseExpression(receiver: Expression): Expression {
		var receiver = receiver
		while (ctx.match(BANG)) {
			if (receiver !is NonNullAssertionExpression) {
				receiver = NonNullAssertionExpression(receiver)
			}
		}
		while (isInvoke()) {
			receiver = parseInvokeExpression(receiver)
		}
		while (ctx.match(BANG)) {
			if (receiver !is NonNullAssertionExpression) {
				receiver = NonNullAssertionExpression(receiver)
			}
		}
		return receiver
	}
}

private val accessTokenTypes = listOf(
	DOT,
	QUESTION_DOT,
	DOUBLE_COLON
)

fun isAccessOperator(ctx: FreeParserContext): Boolean {
	return accessTokenTypes.any { ctx.match(it) }
}