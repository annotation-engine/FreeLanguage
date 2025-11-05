package free.core.lexer.recognizer

import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType

data object WhiteSpaceRecognizer : TokenRecognizer {
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
		if (input[start] != ' ') return null
		var position = start + 1
		while (position < input.size && input[position] == ' ') {
			position++
		}
		return FreeToken(FreeTokenType.WHITE_SPACE, "", start, position, line, column)
	}
}