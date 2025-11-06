package free.core.lexer.recognizer

import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType
import free.core.util.startsWith

data object KeywordRecognizer : TokenRecognizer {
	
	private val keywordTokenTypeMap = mapOf(
		"class" to FreeTokenType.CLASS,
		"single" to FreeTokenType.SINGLE,
		"interface" to FreeTokenType.INTERFACE,
		"enum" to FreeTokenType.ENUM,
		"annotation" to FreeTokenType.ANNOTATION,
		"struct" to FreeTokenType.STRUCT,
		"serial" to FreeTokenType.SERIAL,
		"public" to FreeTokenType.PUBLIC,
		"module" to FreeTokenType.MODULE,
		"local" to FreeTokenType.LOCAL,
		"file" to FreeTokenType.FILE,
		"private" to FreeTokenType.PRIVATE,
		"open" to FreeTokenType.OPEN,
		"abstract" to FreeTokenType.ABSTRACT,
		"only" to FreeTokenType.ONLY,
		"final" to FreeTokenType.FINAL,
		"override" to FreeTokenType.OVERRIDE,
		"super" to FreeTokenType.SUPER,
		"with" to FreeTokenType.WITH,
		"init" to FreeTokenType.INIT,
		"delete" to FreeTokenType.DELETE,
		"var" to FreeTokenType.VAR,
		"val" to FreeTokenType.VAL,
		"const" to FreeTokenType.CONST,
		"fun" to FreeTokenType.FUN,
		"if" to FreeTokenType.IF,
		"else" to FreeTokenType.ELSE,
		"match" to FreeTokenType.MATCH,
		"for" to FreeTokenType.FOR,
		"while" to FreeTokenType.WHILE,
		"do" to FreeTokenType.DO,
		"return" to FreeTokenType.RETURN,
		"break" to FreeTokenType.BREAK,
		"continue" to FreeTokenType.CONTINUE,
		"as" to FreeTokenType.AS,
		"is" to FreeTokenType.IS,
		"package" to FreeTokenType.PACKAGE,
		"import" to FreeTokenType.IMPORT,
		"this" to FreeTokenType.THIS,
		"mut" to FreeTokenType.MUT,
		"true" to FreeTokenType.TRUE,
		"false" to FreeTokenType.FALSE,
		"null" to FreeTokenType.NULL,
	)
	
	private val keywords = keywordTokenTypeMap.keys
	
	override suspend fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
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
			return FreeToken(tokenType, "", start, end, line, column)
		}
		return null
	}
}