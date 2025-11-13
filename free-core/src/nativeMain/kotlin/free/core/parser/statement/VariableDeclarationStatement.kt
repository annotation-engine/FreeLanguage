package free.core.parser.statement

import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext
import free.core.parser.expression.Expression
import free.core.parser.matcher.ExpressionMatcher
import free.core.parser.node.TypeReference
import free.core.parser.node.TypeReferenceParser
import kotlinx.serialization.Serializable

@Serializable
data class VariableDeclarationStatement(
	val name: String,
	val initializer: Expression?,
	val isVariable: Boolean,
	val type: TypeReference?
) : Statement

class VariableDeclarationStatementParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): VariableDeclarationStatement {
		val isVariable = ctx.previous.type == VAR
		ctx.expect(IDENTIFIER, "变量缺少名称")
		val name = ctx.previous.value
		val type = if (ctx.match(COLON)) TypeReferenceParser(ctx).parse() else null
		val initializer = if (ctx.match(ASSIGN)) {
			var expression: Expression? = null
			do {
				expression = ExpressionMatcher.parse(ctx, expression)
			} while (!isStatementEnd(ctx))
			expression
		} else null
		return VariableDeclarationStatement(
			name = name,
			initializer = initializer,
			isVariable = isVariable,
			type = type
		)
	}
	
	private val nonEndTokens = listOf(
		PLUS, MINUS, STAR, SLASH, PERCENT, DOUBLE_STAR,
		EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS, TRIPLE_EQUALS,
		IN, NOT_IN,
		BIT_AND, BIT_OR, BIT_XOR,
		SHL, SHR, USHR,
		AND, OR
	)
	
	private fun isStatementEnd(ctx: FreeParserContext): Boolean {
		if (ctx.match(SEMICOLON)) return true
		val previous = ctx.previous
		val current = ctx.current
		if (previous.line == current.line) return false
		nonEndTokens.forEach {
			if (current.type == it) return false
		}
		return true
	}
}