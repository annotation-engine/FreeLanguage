package free.core.lexer.recognizer

import free.core.FreeContext
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType

data object NewlineRecognizer : TokenRecognizer {
	
	context(_: FreeContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
		val char = input[start]
		if (char != '\n') return null
		return FreeToken(FreeTokenType.NEWLINE, "", start, start + 1, line, column)
	}
}