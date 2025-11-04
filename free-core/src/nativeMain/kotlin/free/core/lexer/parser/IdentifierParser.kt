package free.core.lexer.parser

import free.core.lexer.Token
import free.core.lexer.TokenType

data object IdentifierParser : TokenParser {
	
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token? {
		val char = input[start]
		if (!char.isEnglishLetter() && char != '_') return null
		var position = start + 1
		while (position < input.size && (input[position] == '_' || input[position].isEnglishLetter() || input[position].isDigit())) {
			position++
		}
		return Token(TokenType.IDENTIFIER, input.concatToString(start, position), start, position, line, column)
	}
	
	private fun Char.isEnglishLetter(): Boolean {
		return this in 'a'..'z' || this in 'A'..'Z'
	}
}