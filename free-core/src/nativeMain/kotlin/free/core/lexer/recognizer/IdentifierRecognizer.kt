package free.core.lexer.recognizer

import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType

data object IdentifierRecognizer : TokenRecognizer {
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
		val char = input[start]
		if (!char.isEnglishLetter() && char != '_') return null
		var position = start + 1
		while (position < input.size && (input[position] == '_' || input[position].isEnglishLetter() || input[position].isDigit())) {
			position++
		}
		return FreeToken(FreeTokenType.IDENTIFIER, input.concatToString(start, position), start, position, line, column)
	}
	
	private fun Char.isEnglishLetter(): Boolean {
		return this in 'a'..'z' || this in 'A'..'Z'
	}
}