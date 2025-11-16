package free.core.exception

import free.core.FreeContext
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType

private class SyntaxException(
	message: String,
	sourcePath: String,
	line: Int,
	column: Int,
	type: FreeTokenType?
) : Exception("错误位置: $sourcePath:$line:$column ${if (type != null) "$type " else ""}$message.")

context(context: FreeContext)
fun syntaxError(message: String, line: Int, column: Int, type: FreeTokenType? = null): Nothing {
	throw SyntaxException(message, context.sourcePath, line, column, type)
}

context(context: FreeContext)
fun syntaxError(message: String, token: FreeToken): Nothing {
	throw SyntaxException(message, context.sourcePath, token.line, token.column, token.type)
}