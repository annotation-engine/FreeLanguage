package free.core.lexer.parser

import free.core.exception.syntaxError
import free.core.lexer.Token
import free.core.lexer.TokenType
import free.core.util.EscapeType
import free.core.util.isHex

data object StringParser : TokenParser {
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token? {
		if (input[start] != '"') return null
		val sb = StringBuilder()
		var position = start + 1
		var close = false
		while (position < input.size) {
			when (val c = input[position]) {
				'\\' -> {
					when (val escape = input.concatToString(position, position + 2)) {
						in EscapeType.standardEscapes -> {
							sb.append(escape)
							position += 2
						}
						
						EscapeType.UNICODE.escape -> {
							val unicode = input.concatToString(position + 2, position + 6)
							if (!unicode.isHex()) {
								syntaxError("Illegal escape character: '\\u$unicode'", line, column)
							}
							sb.append("\\u$unicode")
							position += 6
						}
						
						else -> {
							syntaxError("Illegal escape character: '$escape'", line, column)
						}
					}
				}
				
				'\"' -> {
					position++
					close = true
					break
				}
				
				else -> {
					sb.append(c)
					position++
				}
			}
		}
		if (!close) {
			syntaxError("The string is not closed", line, column)
		}
		return Token(TokenType.STRING, sb.toString(), start, position, line, column)
	}
}