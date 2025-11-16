package free.core.parser.statement.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.parser.FreeParserContext
import free.core.parser.statement.Statement

sealed interface StatementMatcher<S : Statement> {
	
	fun match(ctx: FreeParserContext): Boolean
	
	context(_: FreeContext)
	fun parse(ctx: FreeParserContext): S
}

private val matchers = listOf(
	VariableDeclarationStatementMatcher
)

context(_: FreeContext)
fun parseStatement(ctx: FreeParserContext): Statement {
	val matcher = matchers.find { it.match(ctx) }
		?: syntaxError("不支持的语句", ctx.current)
	return matcher.parse(ctx)
}