package free.core.lexer.recognizer

import free.core.FreeContext
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType

data object CommentRecognizer : TokenRecognizer {
	
	context(_: FreeContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
		if (input[start] != '#') return null
		var position = start + 1
		while (position < input.size && input[position] != '\n') {
			position++
		}
		val comment = input.concatToString(start + 1, position).trim()
		return FreeToken(FreeTokenType.COMMENT, comment, start, position, line, column)
	}
}