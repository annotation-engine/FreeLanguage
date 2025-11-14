package free.core.parser.statement.matcher

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.statement.VariableDeclarationStatement
import free.core.parser.statement.parser.VariableDeclarationStatementParser

object VariableDeclarationStatementMatcher : StatementMatcher<VariableDeclarationStatement> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.VAR) || ctx.match(FreeTokenType.VAL)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext): VariableDeclarationStatement {
		return VariableDeclarationStatementParser(ctx).parse()
	}
}