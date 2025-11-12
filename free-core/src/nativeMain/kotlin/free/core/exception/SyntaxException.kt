package free.core.exception

import free.core.FreeContext
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType
import kotlinx.coroutines.currentCoroutineContext

private class SyntaxException(
	message: String,
	sourcePath: String,
	line: Int,
	column: Int,
	type: FreeTokenType?
) : Exception("错误位置:$sourcePath:$line:$column ${if (type != null) "$type " else ""}$message.")

suspend fun syntaxError(message: String, line: Int, column: Int, type: FreeTokenType? = null): Nothing {
	val sourcePath = currentCoroutineContext()[FreeContext]!!.sourcePath
	throw SyntaxException(message, sourcePath, line, column, type)
}

suspend inline fun syntaxError(message: String, token: FreeToken): Nothing {
	syntaxError(message, token.line, token.column, token.type)
}