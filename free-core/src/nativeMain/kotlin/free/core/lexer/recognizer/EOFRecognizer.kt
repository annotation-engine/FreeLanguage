package free.core.lexer.recognizer

import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType

data object EOFRecognizer : TokenRecognizer {
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
		if (start < input.size) return null
		return FreeToken(FreeTokenType.EOF, "", start, start + 1, line, column)
	}
}