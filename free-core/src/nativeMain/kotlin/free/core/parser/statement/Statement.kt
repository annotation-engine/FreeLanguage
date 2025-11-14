package free.core.parser.statement

import free.core.lexer.FreeTokenType.*
import free.core.parser.FreeParserContext
import kotlinx.serialization.Serializable

@Serializable
sealed interface Statement

/**
 * 是否是表达式结束
 */
fun isStatementEnd(ctx: FreeParserContext): Boolean {
	if (ctx.match(SEMICOLON)) return true
	val previous = ctx.previous
	val current = ctx.current
	if (previous.line == current.line) return false
	return previous.line < current.line && current.type != AND && current.type != OR
}