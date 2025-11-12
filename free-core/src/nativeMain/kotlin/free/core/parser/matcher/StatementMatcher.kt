package free.core.parser.matcher

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.statement.Statement
import free.core.parser.statement.VariableDeclarationStatement
import free.core.parser.statement.VariableDeclarationStatementParser

sealed interface StatementMatcher<S : Statement> {
	
	val tokenTypes: List<FreeTokenType>
		get() = emptyList()
	
	fun match(ctx: FreeParserContext): Boolean {
		tokenTypes.forEach {
			if (ctx.match(it)) return true
		}
		return false
	}
	
	suspend fun parse(ctx: FreeParserContext): S
	
	companion object {
		
		private val matchers = listOf(
			VariableDeclarationStatementMatcher
		)
		
		suspend fun parse(ctx: FreeParserContext): Statement {
			matchers.forEach {
				if (it.match(ctx)) return it.parse(ctx)
			}
			syntaxError("不支持的语句", ctx.current)
		}
	}
}

private object VariableDeclarationStatementMatcher : StatementMatcher<VariableDeclarationStatement> {
	
	override val tokenTypes = listOf(FreeTokenType.VAR, FreeTokenType.VAL)
	
	override suspend fun parse(ctx: FreeParserContext): VariableDeclarationStatement {
		return VariableDeclarationStatementParser(ctx).parse()
	}
}