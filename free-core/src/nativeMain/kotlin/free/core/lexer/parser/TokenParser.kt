package free.core.lexer.parser

import free.core.lexer.Token

sealed interface TokenParser {
	
	fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token?
}