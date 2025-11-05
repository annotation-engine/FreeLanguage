package free.core.lexer.recognizer

import free.core.lexer.FreeToken

sealed interface TokenRecognizer {
	
	suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken?
}