package free.core.lexer.parser

import free.core.lexer.Token
import free.core.lexer.TokenType

data object EOFParser : TokenParser {
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token? {
		if (start < input.size) return null
		return Token(TokenType.EOF, "", input.size, input.size, line, column)
	}
}