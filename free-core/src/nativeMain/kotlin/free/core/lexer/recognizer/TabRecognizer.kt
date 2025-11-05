package free.core.lexer.recognizer

import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType

data object TabRecognizer : TokenRecognizer {
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
		if (input[start] != '\t') return null
		var position = start + 1
		while (position < input.size && input[position] == '\t') {
			position++
		}
		return FreeToken(FreeTokenType.TAB, "", start, position, line, column)
	}
}