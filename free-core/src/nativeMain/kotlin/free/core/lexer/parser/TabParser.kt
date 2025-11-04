package free.core.lexer.parser

import free.core.lexer.Token
import free.core.lexer.TokenType

data object TabParser : TokenParser {
	
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token? {
		if (input[start] != '\t') return null
		var position = start + 1
		while (position < input.size && input[position] == '\t') {
			position++
		}
		return Token(TokenType.TAB, "", start, position, line, column)
	}
}