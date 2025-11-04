package free.core.lexer

import free.core.exception.syntaxError
import free.core.io.File
import free.core.lexer.parser.*
import free.core.util.TAB_LENGTH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

suspend fun CoroutineScope.lexers(paths: List<String>): List<List<Token>> {
	val files = paths.toSet().map { File(it) }.distinctBy { it.absolutePath }
	val jobs = files.map { file ->
		val path = file.absolutePath
		async(Dispatchers.Default + SourcePathContext(path)) {
			val input = file.readFileChars()
			lexer(input)
		}
	}
	return jobs.awaitAll()
}

class SourcePathContext(
	val path: String
) : AbstractCoroutineContextElement(Key) {
	companion object Key : CoroutineContext.Key<SourcePathContext>
}

/**
 * 词法分析器
 */
private suspend fun lexer(input: CharArray): List<Token> {
	val tokens = mutableListOf<Token>()
	val lexer = Lexer(input)
	while (true) {
		val token = lexer.nextToken()
		tokens += token
		if (token.type == TokenType.EOF) break
	}
	return tokens
}

private class Lexer(
	private val input: CharArray,
) {
	
	private var position = 0
	private var line = 1
	private var column = 1
	
	private val parsers = listOf(
		EOFParser,
		WhiteSpaceParser,
		TabParser,
		NewlineParser,
		CommentParser,
		KeywordParser,
		CharParser,
		StringParser,
		NumberParser,
		SymbolParser,
		IdentifierParser,
	)
	
	suspend fun nextToken(): Token {
		parsers.forEach {
			val token = it.tryParse(input, position, line, column) ?: return@forEach
			position = token.end
			column += if (token.type != TokenType.TAB) token.length else token.length * TAB_LENGTH
			when (token.type) {
				TokenType.NEWLINE -> {
					column = 1
					line++
					return nextToken()
				}
				
				TokenType.WHITE_SPACE, TokenType.TAB -> {
					return nextToken()
				}
				
				else -> return token
			}
		}
		syntaxError("${input[position]} is an invalid token. position = $position", line, column)
	}
}