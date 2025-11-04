package free.core.exception

private class SyntaxException(
	message: String,
	line: Int,
	column: Int,
) : Exception("$message at $line:$column")

fun syntaxError(message: String, line: Int, column: Int): Nothing {
	throw SyntaxException(message, line, column)
}