package free.core.lexer

import free.core.exception.syntaxError
import free.core.lexer.parser.*
import free.core.util.TAB_LENGTH

/**
 * 词法分析器
 */
fun lexer(input: CharArray): List<Token> {
	val tokens = mutableListOf<Token>()
	val lexer = Lexer(input)
	while (true) {
		val token = lexer.nextToken()
		tokens += token
		if (token.type == TokenType.EOF) break
	}
	return tokens
}

private class Lexer(
	private val input: CharArray,
) {
	
	private var position = 0
	private var line = 1
	private var column = 1
	
	private val parsers = listOf(
		EOFParser,
		WhiteSpaceParser,
		TabParser,
		NewlineParser,
		CommentParser,
		KeywordParser,
		CharParser,
		StringParser,
		NumberParser,
		SymbolParser,
		IdentifierParser,
	)
	
	fun nextToken(): Token {
		parsers.forEach {
			val token = it.tryParse(input, position, line, column) ?: return@forEach
			position = token.end
			column += if (token.type != TokenType.TAB) token.length else token.length * TAB_LENGTH
			when (token.type) {
				TokenType.NEWLINE -> {
					column = 1
					line++
					return nextToken()
				}
				
				TokenType.WHITE_SPACE, TokenType.TAB -> {
					return nextToken()
				}
				
				else -> return token
			}
		}
		syntaxError("${input[position]} is an invalid token. position = $position", line, column)
	}
}