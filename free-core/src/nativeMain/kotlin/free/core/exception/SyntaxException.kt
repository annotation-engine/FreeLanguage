package free.core.exception

import free.core.lexer.SourcePathContext
import kotlinx.coroutines.currentCoroutineContext

private class SyntaxException(
	message: String,
	path: String,
	line: Int,
	column: Int,
) : Exception("e: file:$path:$line:$column $message.")

suspend fun syntaxError(message: String, line: Int, column: Int): Nothing {
	val path = currentCoroutineContext()[SourcePathContext]!!.path
	throw SyntaxException(message, path, line, column)
}