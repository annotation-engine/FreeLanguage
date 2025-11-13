package free.core.lexer

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.recognizer.*
import free.core.util.TAB_LENGTH

class FreeLexer(
	private val input: CharArray
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
	
	context(_: FreeContext)
	fun lex(): List<FreeToken> {
		return buildList {
			while (true) {
				val token = nextToken()
				this += token
				if (token.type == FreeTokenType.EOF) break
			}
		}
	}
	
	context(_: FreeContext)
	private fun nextToken(): FreeToken {
		recognizers.forEach {
			val token = it.tryParse(input, position, line, column) ?: return@forEach
			position = token.end
			column += if (token.type != FreeTokenType.TAB) token.length else token.length * TAB_LENGTH
			when (token.type) {
				FreeTokenType.NEWLINE -> {
					column = 1
					line++
					return nextToken()
				}
				
				FreeTokenType.WHITE_SPACE, FreeTokenType.TAB -> {
					return nextToken()
				}
				
				else -> return token
			}
		}
		syntaxError("${input[position]} 无法被识别", line, column)
	}
}