package free.core.lexer.parser

import free.core.lexer.Token
import free.core.lexer.TokenType

data object SymbolParser : TokenParser {
	
	private val tokenTypeMap = mapOf(
		3 to mapOf(
			">>>" to TokenType.USHR,
			"!~>" to TokenType.NOT_IN,
			"===" to TokenType.TRIPLE_EQUAL
		),
		2 to mapOf(
			"**" to TokenType.DOUBLE_STAR,
			"++" to TokenType.DOUBLE_PLUS,
			"--" to TokenType.DOUBLE_MINUS,
			"+=" to TokenType.PLUS_ASSIGN,
			"-=" to TokenType.MINUS_ASSIGN,
			"*=" to TokenType.STAR_ASSIGN,
			"/=" to TokenType.SLASH_ASSIGN,
			"%=" to TokenType.PERCENT_ASSIGN,
			"==" to TokenType.EQUALS,
			"!=" to TokenType.NOT_EQUALS,
			">=" to TokenType.GT_EQUALS,
			"<=" to TokenType.LT_EQUALS,
			"&&" to TokenType.AND,
			"||" to TokenType.OR,
			"<<" to TokenType.SHL,
			">>" to TokenType.SHR,
			"~>" to TokenType.IN,
			"?:" to TokenType.ELVIS,
			"->" to TokenType.ARROW,
			".." to TokenType.DOUBLE_DOT,
			"?=" to TokenType.QUESTION_ASSIGN,
			"?." to TokenType.QUESTION_DOT,
			"!!" to TokenType.NOT_NULL_ASSERT,
		),
		1 to mapOf(
			"+" to TokenType.PLUS,
			"-" to TokenType.MINUS,
			"*" to TokenType.STAR,
			"/" to TokenType.SLASH,
			"%" to TokenType.PERCENT,
			"=" to TokenType.ASSIGN,
			">" to TokenType.GT,
			"<" to TokenType.LT,
			"!" to TokenType.NOT,
			"&" to TokenType.BIT_AND,
			"|" to TokenType.BIT_OR,
			"^" to TokenType.BIT_XOR,
			"~" to TokenType.BIT_NOT,
			"." to TokenType.DOT,
			"," to TokenType.COMMA,
			";" to TokenType.SEMICOLON,
			":" to TokenType.COLON,
			"?" to TokenType.QUESTION,
			"@" to TokenType.AT,
			"(" to TokenType.LPAREN,
			")" to TokenType.RPAREN,
			"[" to TokenType.LBRACKET,
			"]" to TokenType.RBRACKET,
			"{" to TokenType.LBRACE,
			"}" to TokenType.RBRACE,
		)
	)
	
	private val maxLength = tokenTypeMap.keys.maxOf { it }
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token? {
		if (input[start].toString() !in tokenTypeMap[1]!!) return null
		for (length in maxLength downTo 1) {
			if (start + length > input.size) continue
			val symbol = input.concatToString(start, start + length)
			val tokenType = tokenTypeMap[length]!![symbol] ?: continue
			return Token(tokenType, symbol, start, start + length, line, column)
		}
		return null
	}
}