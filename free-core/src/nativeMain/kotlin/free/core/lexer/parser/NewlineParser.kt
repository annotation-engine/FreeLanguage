package free.core.lexer.parser

import free.core.lexer.Token
import free.core.lexer.TokenType

data object NewlineParser : TokenParser {
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token? {
		val char = input[start]
		if (char != '\n') return null
		return Token(TokenType.NEWLINE, "", start, start + 1, line, column)
	}
}