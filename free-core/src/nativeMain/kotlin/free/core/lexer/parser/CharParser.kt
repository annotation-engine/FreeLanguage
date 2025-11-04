package free.core.lexer.parser

import free.core.exception.syntaxError
import free.core.lexer.Token
import free.core.lexer.TokenType
import free.core.util.EscapeType
import free.core.util.isHex

data object CharParser : TokenParser {
	
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token? {
		if (input[start] != '\'') return null
		if (start + 2 < input.size && input[start + 2] == '\'' && input[start + 1] != '\\') {
			return Token(TokenType.CHAR, input[start + 1].toString(), start, start + 3, line, column)
		}
		if (start + 3 < input.size && input[start + 3] == '\'' && input[start + 1] == '\\') {
			val escape = input.concatToString(start + 1, start + 3)
			if (escape in EscapeType.standardEscapes) {
				return Token(TokenType.CHAR, escape, start, start + 4, line, column)
			} else {
				syntaxError("Illegal escape character: '$escape'", line, column)
			}
		}
		if (start + 7 < input.size && input[start + 7] == '\'' && input.concatToString(start + 1, start + 3) == EscapeType.UNICODE.escape) {
			val unicode = input.concatToString(start + 3, start + 7)
			if (!unicode.isHex()) {
				syntaxError("Illegal escape character: '\\u$unicode'", line, column)
			}
			return Token(TokenType.CHAR, "\\u$unicode", start, start + 8, line, column)
		}
		syntaxError("Syntax error", line, column)
	}
}