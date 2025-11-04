package free.core.lexer.parser

import free.core.lexer.Token

sealed interface TokenParser {
	
	suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token?
}