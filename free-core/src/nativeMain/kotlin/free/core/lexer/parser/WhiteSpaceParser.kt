package free.core.lexer.parser

import free.core.lexer.Token
import free.core.lexer.TokenType

data object WhiteSpaceParser : TokenParser {
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token? {
		if (input[start] != ' ') return null
		var position = start + 1
		while (position < input.size && input[position] == ' ') {
			position++
		}
		return Token(TokenType.WHITE_SPACE, "", start, position, line, column)
	}
}