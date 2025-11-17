package free.core.parser.statement.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
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
		?: run {
			if (ctx.current.type == FreeTokenType.EOF) {
				syntaxError("函数缺少 '}'", ctx.current)
			} else {
				syntaxError("不支持的语句", ctx.current)
			}
		}
	return matcher.parse(ctx)
}