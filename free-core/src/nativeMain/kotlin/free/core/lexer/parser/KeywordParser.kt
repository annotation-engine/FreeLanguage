package free.core.lexer.parser

import free.core.lexer.Token
import free.core.lexer.TokenType
import free.core.util.startsWith

data object KeywordParser : TokenParser {
	
	private val keywordTokenTypeMap = mapOf(
		"class" to TokenType.CLASS,
		"single" to TokenType.SINGLE,
		"interface" to TokenType.INTERFACE,
		"enum" to TokenType.ENUM,
		"annotation" to TokenType.ANNOTATION,
		"struct" to TokenType.STRUCT,
		"serial" to TokenType.SERIAL,
		"public" to TokenType.PUBLIC,
		"private" to TokenType.PRIVATE,
		"file" to TokenType.FILE,
		"open" to TokenType.OPEN,
		"abstract" to TokenType.ABSTRACT,
		"only" to TokenType.ONLY,
		"final" to TokenType.FINAL,
		"override" to TokenType.OVERRIDE,
		"super" to TokenType.SUPER,
		"with" to TokenType.WITH,
		"init" to TokenType.INIT,
		"delete" to TokenType.DELETE,
		"var" to TokenType.VAR,
		"val" to TokenType.VAL,
		"const" to TokenType.CONST,
		"fun" to TokenType.FUN,
		"if" to TokenType.IF,
		"else" to TokenType.ELSE,
		"match" to TokenType.MATCH,
		"for" to TokenType.FOR,
		"while" to TokenType.WHILE,
		"do" to TokenType.DO,
		"return" to TokenType.RETURN,
		"break" to TokenType.BREAK,
		"continue" to TokenType.CONTINUE,
		"as" to TokenType.AS,
		"is" to TokenType.IS,
		"package" to TokenType.PACKAGE,
		"import" to TokenType.IMPORT,
		"this" to TokenType.THIS,
		"mut" to TokenType.MUT,
		"true" to TokenType.TRUE,
		"false" to TokenType.FALSE,
		"null" to TokenType.NULL,
	)
	
	private val keywords = keywordTokenTypeMap.keys
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): Token? {
		for (keyword in keywords) {
			if (!input.startsWith(keyword, start)) {
				continue
			}
			val end = start + keyword.length
			val nextChar = input.getOrNull(end)
			if (nextChar != null && (nextChar.isLetterOrDigit() || nextChar == '_')) {
				continue
			}
			val tokenType = keywordTokenTypeMap[keyword]!!
			return Token(tokenType, keyword, start, end, line, column)
		}
		return null
	}
}