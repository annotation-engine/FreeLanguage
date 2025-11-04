package free.core.lexer.parser

import free.core.lexer.Token
import free.core.lexer.TokenType

data object CommentParser : TokenParser {
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token? {
		if (input[start] != '#') return null
		var position = start + 1
		while (position < input.size && input[position] != '\n') {
			position++
		}
		val comment = input.concatToString(start + 1, position).trim()
		return Token(TokenType.COMMENT, comment, start, position, line, column)
	}
}