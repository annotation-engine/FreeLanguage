package free.core.parser.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.statement.Statement
import free.core.parser.statement.VariableDeclarationStatement
import free.core.parser.statement.VariableDeclarationStatementParser

sealed interface StatementMatcher<S : Statement> {
	
	fun match(ctx: FreeParserContext): Boolean
	
	context(_: FreeContext)
	fun parse(ctx: FreeParserContext): S
	
	companion object {
		
		private val matchers = listOf(
			VariableDeclarationStatementMatcher
		)
		
		context(_: FreeContext)
		fun parse(ctx: FreeParserContext): Statement {
			matchers.forEach {
				if (it.match(ctx)) return it.parse(ctx)
			}
			syntaxError("不支持的语句", ctx.current)
		}
	}
}

private object VariableDeclarationStatementMatcher : StatementMatcher<VariableDeclarationStatement> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.VAR) || ctx.match(FreeTokenType.VAL)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext): VariableDeclarationStatement {
		return VariableDeclarationStatementParser(ctx).parse()
	}
}