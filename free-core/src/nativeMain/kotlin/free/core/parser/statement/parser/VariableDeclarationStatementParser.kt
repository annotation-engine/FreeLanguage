package free.core.parser.statement.parser

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.matcher.parseCompleteExpression
import free.core.parser.node.TypeReferenceParser
import free.core.parser.statement.VariableDeclarationStatement

class VariableDeclarationStatementParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): VariableDeclarationStatement {
		val isVariable = ctx.previous.type == FreeTokenType.VAR
		ctx.expect(FreeTokenType.IDENTIFIER, "变量缺少名称")
		val name = ctx.previous.value
		val type = if (ctx.match(FreeTokenType.COLON)) TypeReferenceParser(ctx).parse() else null
		val initializer = if (ctx.match(FreeTokenType.ASSIGN)) {
			parseCompleteExpression(ctx)
		} else null
		return VariableDeclarationStatement(
			name = name,
			initializer = initializer,
			isVariable = isVariable,
			type = type
		)
	}
}