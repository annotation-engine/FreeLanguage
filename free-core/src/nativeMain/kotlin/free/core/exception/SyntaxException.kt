package free.core.exception

import free.core.FreeContext
import free.core.lexer.FreeToken
import kotlinx.coroutines.currentCoroutineContext

private class SyntaxException(
	message: String,
	sourcePath: String,
	line: Int,
	column: Int,
) : Exception("错误位置:$sourcePath:$line:$column $message.")

suspend fun syntaxError(message: String, line: Int, column: Int): Nothing {
	val sourcePath = currentCoroutineContext()[FreeContext]!!.sourcePath
	throw SyntaxException(message, sourcePath, line, column)
}

suspend inline fun syntaxError(message: String, token: FreeToken): Nothing {
	syntaxError(message, token.line, token.column)
}