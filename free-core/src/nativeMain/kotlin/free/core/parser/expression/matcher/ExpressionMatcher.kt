package free.core.parser.expression.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression

sealed interface ExpressionMatcher<out E : Expression> {
	
	fun match(ctx: FreeParserContext, left: Expression?): Boolean
	
	context(_: FreeContext)
	fun parse(ctx: FreeParserContext, left: Expression?): E
}

private val matchers = listOf(
	GroupingExpressionMatcher,
	PrefixUnaryExpressionMatcher,
	SuffixUnaryExpressionMatcher,
	PostfixExpressionMatcher,
	BinaryExpressionMatcher,
	ElvisExpressionMatcher,
	TernaryExpressionMatcher
)

context(_: FreeContext)
fun parseExpression(ctx: FreeParserContext, left: Expression? = null): Expression {
	val matcher = matchers.find { it.match(ctx, left) }
		?: syntaxError("不支持的表达式", ctx.current)
	return matcher.parse(ctx, left)
}

context(_: FreeContext)
fun parseCompleteExpression(ctx: FreeParserContext): Expression {
	var expression: Expression? = null
	do {
		expression = parseExpression(ctx, expression)
	} while (!isAtExpressionEnd(ctx))
	return expression
}

private val endTokenTypes = setOf(SEMICOLON)

private val nonConsumerEndTokenTypes = setOf(RPAREN, RBRACKET, COLON, COMMA)

private fun isAtExpressionEnd(ctx: FreeParserContext): Boolean {
	val exists = endTokenTypes.any { ctx.match(it) } ||
			nonConsumerEndTokenTypes.any { ctx.check(it) }
	if (exists) return true
	val previous = ctx.previous
	val current = ctx.current
	if (previous.line == current.line) return false
	return previous.line < current.line && current.type != AND && current.type != OR
}