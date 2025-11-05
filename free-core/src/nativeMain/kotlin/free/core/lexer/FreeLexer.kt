package free.core.lexer

import free.core.exception.syntaxError
import free.core.io.File
import free.core.lexer.recognizer.*
import free.core.util.TAB_LENGTH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

suspend fun CoroutineScope.lexerToTokens(paths: List<String>): List<List<FreeToken>> {
	val files = paths.toSet().map { File(it) }.distinctBy { it.absolutePath }
	val jobs = files.map { file ->
		val sourcePath = file.absolutePath
		async(Dispatchers.Default + FreeContext(sourcePath)) {
			val input = file.readFileChars()
			FreeLexer(input).lexer().also {
				println(it.formatToString())
			}
		}
	}
	return jobs.awaitAll()
}

class FreeContext(
	val sourcePath: String
) : AbstractCoroutineContextElement(Key) {
	companion object Key : CoroutineContext.Key<FreeContext>
}

private class FreeLexer(
	private val input: CharArray,
) {
	
	private var position = 0
	private var line = 1
	private var column = 1
	
	private val recognizers = listOf(
		EOFRecognizer,
		WhiteSpaceRecognizer,
		TabRecognizer,
		NewlineRecognizer,
		CommentRecognizer,
		KeywordRecognizer,
		CharRecognizer,
		StringRecognizer,
		NumberRecognizer,
		SymbolRecognizer,
		IdentifierRecognizer,
	)
	
	suspend fun lexer(): List<FreeToken> {
		return buildList {
			while (true) {
				val token = nextToken()
				this += token
				if (token.type == FreeTokenType.EOF) break
			}
		}
	}
	
	private suspend fun nextToken(): FreeToken {
		recognizers.forEach {
			val token = it.tryParse(input, position, line, column) ?: return@forEach
			position = token.end
			column += if (token.type != FreeTokenType.TAB) token.length else token.length * TAB_LENGTH
			when (token.type) {
				FreeTokenType.NEWLINE -> {
					column = 1
					line++
					return token
				}
				
				FreeTokenType.WHITE_SPACE, FreeTokenType.TAB -> {
					return nextToken()
				}
				
				else -> return token
			}
		}
		syntaxError("${input[position]} is an invalid token. position = $position", line, column)
	}
}