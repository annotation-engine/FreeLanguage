package free.core.parser.statement

import free.core.FreeContext
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
	
	context(_: FreeContext)
	fun parse(): VariableDeclarationStatement {
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
}