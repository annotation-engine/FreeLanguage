package free.core.lexer.recognizer

import free.core.exception.syntaxError
import free.core.FreeContext
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType

data object IdentifierRecognizer : TokenRecognizer {
	
	context(_: FreeContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
		val char = input[start]
		if (!char.isEnglishLetter() && char != '_') return null
		var position = start + 1
		while (position < input.size && (input[position] == '_' || input[position].isEnglishLetter() || input[position].isDigit())) {
			position++
		}
		val identifier = input.concatToString(start, position)
		var isValid = identifier == "_"
		for (c in identifier) {
			if (isValid) break
			isValid = c != '_'
		}
		if (!isValid) {
			syntaxError("不合法的标识符", line, column)
		}
		return FreeToken(FreeTokenType.IDENTIFIER, identifier, start, position, line, column)
	}
	
	private fun Char.isEnglishLetter(): Boolean {
		return this in 'a'..'z' || this in 'A'..'Z'
	}
}