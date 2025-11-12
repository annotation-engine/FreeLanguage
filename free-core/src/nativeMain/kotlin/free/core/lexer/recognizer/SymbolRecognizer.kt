package free.core.lexer.recognizer

import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType

data object SymbolRecognizer : TokenRecognizer {
	
	private val tokenTypeMap = mapOf(
		3 to mapOf(
			">>>" to FreeTokenType.USHR,
			"===" to FreeTokenType.TRIPLE_EQUALS,
			"!==" to FreeTokenType.TRIPLE_NOT_EQUALS
		),
		2 to mapOf(
			"**" to FreeTokenType.DOUBLE_STAR,
			"++" to FreeTokenType.DOUBLE_PLUS,
			"--" to FreeTokenType.DOUBLE_MINUS,
			"+=" to FreeTokenType.PLUS_ASSIGN,
			"-=" to FreeTokenType.MINUS_ASSIGN,
			"*=" to FreeTokenType.STAR_ASSIGN,
			"/=" to FreeTokenType.SLASH_ASSIGN,
			"%=" to FreeTokenType.PERCENT_ASSIGN,
			"==" to FreeTokenType.EQUALS,
			"!=" to FreeTokenType.NOT_EQUALS,
			">=" to FreeTokenType.GT_EQUALS,
			"<=" to FreeTokenType.LT_EQUALS,
			"&&" to FreeTokenType.AND,
			"||" to FreeTokenType.OR,
			"<<" to FreeTokenType.SHL,
			">>" to FreeTokenType.SHR,
			"~>" to FreeTokenType.IN,
			"!>" to FreeTokenType.NOT_IN,
			"?:" to FreeTokenType.ELVIS,
			"->" to FreeTokenType.ARROW,
			".." to FreeTokenType.DOUBLE_DOT,
			"?=" to FreeTokenType.QUESTION_ASSIGN,
			"?." to FreeTokenType.QUESTION_DOT,
			"!." to FreeTokenType.NOT_NULL_ACCESS,
			"!!" to FreeTokenType.NOT_NULL_ASSERT,
			"::" to FreeTokenType.DOUBLE_COLON
		),
		1 to mapOf(
			"+" to FreeTokenType.PLUS,
			"-" to FreeTokenType.MINUS,
			"*" to FreeTokenType.STAR,
			"/" to FreeTokenType.SLASH,
			"%" to FreeTokenType.PERCENT,
			"=" to FreeTokenType.ASSIGN,
			">" to FreeTokenType.GT,
			"<" to FreeTokenType.LT,
			"!" to FreeTokenType.NOT,
			"&" to FreeTokenType.BIT_AND,
			"|" to FreeTokenType.BIT_OR,
			"^" to FreeTokenType.BIT_XOR,
			"~" to FreeTokenType.BIT_NOT,
			"." to FreeTokenType.DOT,
			"," to FreeTokenType.COMMA,
			";" to FreeTokenType.SEMICOLON,
			":" to FreeTokenType.COLON,
			"?" to FreeTokenType.QUESTION,
			"@" to FreeTokenType.AT,
			"(" to FreeTokenType.LPAREN,
			")" to FreeTokenType.RPAREN,
			"[" to FreeTokenType.LBRACKET,
			"]" to FreeTokenType.RBRACKET,
			"{" to FreeTokenType.LBRACE,
			"}" to FreeTokenType.RBRACE,
		)
	)
	
	private val maxLength = tokenTypeMap.keys.maxOf { it }
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
		if (input[start].toString() !in tokenTypeMap[1]!!) return null
		for (length in maxLength downTo 1) {
			if (start + length > input.size) continue
			val symbol = input.concatToString(start, start + length)
			val tokenType = tokenTypeMap[length]!![symbol] ?: continue
			return FreeToken(tokenType, "", start, start + length, line, column)
		}
		return null
	}
}