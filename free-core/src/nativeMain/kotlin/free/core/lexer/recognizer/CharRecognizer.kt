package free.core.lexer.recognizer

import free.core.exception.syntaxError
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType
import free.core.util.EscapeType
import free.core.util.isHex

data object CharRecognizer : TokenRecognizer {
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
		if (input[start] != '\'') return null
		if (start + 2 < input.size && input[start + 2] == '\'' && input[start + 1] != '\\') {
			return FreeToken(FreeTokenType.CHAR, input[start + 1].toString(), start, start + 3, line, column)
		}
		if (start + 3 < input.size && input[start + 3] == '\'' && input[start + 1] == '\\') {
			val escape = input.concatToString(start + 1, start + 3)
			if (escape in EscapeType.standardEscapes) {
				return FreeToken(FreeTokenType.CHAR, escape, start, start + 4, line, column)
			} else {
				syntaxError("Illegal escape character: '$escape'", line, column)
			}
		}
		if (start + 7 < input.size && input[start + 7] == '\'' && input.concatToString(start + 1, start + 3) == EscapeType.UNICODE.escape) {
			val unicode = input.concatToString(start + 3, start + 7)
			if (!unicode.isHex()) {
				syntaxError("Illegal escape character: '\\u$unicode'", line, column)
			}
			return FreeToken(FreeTokenType.CHAR, "\\u$unicode", start, start + 8, line, column)
		}
		syntaxError("Syntax error", line, column)
	}
}