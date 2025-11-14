package free.core.parser.expression.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext
import free.core.parser.expression.BinaryExpression
import free.core.parser.expression.Expression
import free.core.parser.expression.parser.BinaryExpressionParser

object BinaryExpressionMatcher : ExpressionMatcher<BinaryExpression> {
	
	private val tokenTypes = listOf(
		PLUS, MINUS, STAR, SLASH, PERCENT, DOUBLE_STAR,
		EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS, TRIPLE_EQUALS,
		BIT_AND, BIT_OR, BIT_XOR, SHL, SHR, USHR,
		IN, NOT_IN,
		AND, OR
	)
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		tokenTypes.forEach {
			if (ctx.match(it)) return true
		}
		return false
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): BinaryExpression {
		if (left == null) {
			syntaxError("二元运算符未解析到左值", ctx.peek(offset = -2)!!)
		}
		return BinaryExpressionParser(ctx).parse(left)
	}
}