package free.core.parser.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext
import free.core.parser.expression.*

sealed interface ExpressionMatcher<out E : Expression> {
	
	fun match(ctx: FreeParserContext, left: Expression?): Boolean
	
	context(_: FreeContext)
	fun parse(ctx: FreeParserContext, left: Expression?): E
	
	companion object {
		
		private val matchers = listOf(
			GroupingExpressionMatcher,
			PrefixUnaryExpressionMatcher,
			SuffixUnaryExpressionMatcher,
			BinaryExpressionMatcher,
			PrimaryExpressionMatcher,
		)
		
		context(_: FreeContext)
		fun parse(ctx: FreeParserContext, left: Expression? = null): Expression {
			matchers.forEach {
				if (it.match(ctx, left)) {
					return it.parse(ctx, left)
				}
			}
			syntaxError("不支持的表达式", ctx.current)
		}
	}
}

private object GroupingExpressionMatcher : ExpressionMatcher<GroupingExpression> {
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		return ctx.match(LPAREN)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): GroupingExpression {
		var expression: Expression? = null
		do {
			expression = ExpressionMatcher.parse(ctx, expression)
		} while (!ctx.match(RPAREN))
		return GroupingExpression(expression)
	}
}

private object PrefixUnaryExpressionMatcher : ExpressionMatcher<PrefixUnaryExpression> {
	
	private val tokenTypes = listOf(PLUS, MINUS, NOT, BIT_NOT, DOUBLE_PLUS, DOUBLE_MINUS)
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		tokenTypes.forEach {
			if (ctx.current.type == it) {
				return if (left == null || (it != PLUS && it != MINUS)) {
					ctx.advance()
					true
				} else false
			}
		}
		return false
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): PrefixUnaryExpression {
		return PrefixUnaryExpressionParser(ctx).parse()
	}
}

private object SuffixUnaryExpressionMatcher : ExpressionMatcher<SuffixUnaryExpression> {
	
	private val tokenTypes = listOf(DOUBLE_PLUS, DOUBLE_MINUS)
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		val type = ctx.next.type
		tokenTypes.forEach {
			if (type == it) return true
		}
		return false
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): SuffixUnaryExpression {
		return SuffixUnaryExpressionParser(ctx).parse()
	}
}

private object BinaryExpressionMatcher : ExpressionMatcher<BinaryExpression> {
	
	private val tokenTypes = listOf(
		PLUS, MINUS, STAR, SLASH, PERCENT, DOUBLE_STAR,
		EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS, TRIPLE_EQUALS,
		BIT_AND, BIT_OR, BIT_XOR, SHL, SHR, USHR,
		IN, NOT_IN,
		AND, OR
	)
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		tokenTypes.forEach {
			if (ctx.match(it)) {
				return true
			}
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

private object PrimaryExpressionMatcher : ExpressionMatcher<Expression> {
	
	private val tokenTypes = listOf(NUMBER, STRING, CHAR, TRUE, FALSE, IDENTIFIER)
	
	override fun match(ctx: FreeParserContext, left: Expression?): Boolean {
		tokenTypes.forEach {
			if (ctx.match(it)) return true
		}
		return false
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, left: Expression?): Expression {
		return PrimaryExpressionParser(ctx).parse()
	}
}