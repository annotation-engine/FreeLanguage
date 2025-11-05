package free.core.exception

import free.core.lexer.FreeContext
import kotlinx.coroutines.currentCoroutineContext

private class SyntaxException(
	message: String,
	sourcePath: String,
	line: Int,
	column: Int,
) : Exception("e: file:$sourcePath:$line:$column $message.")

suspend fun syntaxError(message: String, line: Int, column: Int): Nothing {
	val sourcePath = currentCoroutineContext()[FreeContext]!!.sourcePath
	throw SyntaxException(message, sourcePath, line, column)
}