package free.core.lexer.recognizer

import free.core.FreeContext
import free.core.lexer.FreeToken

sealed interface TokenRecognizer {
	
	context(_: FreeContext)
	fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken?
}