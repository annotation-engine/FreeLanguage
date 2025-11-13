package free.core.lexer.recognizer

import free.core.FreeContext
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType
import free.core.util.startsWith

data object KeywordRecognizer : TokenRecognizer {
	
	private val keywordTokenTypeMap = mapOf(
		"fun" to FreeTokenType.FUN,
		"class" to FreeTokenType.CLASS,
		"single" to FreeTokenType.SINGLE,
		"interface" to FreeTokenType.INTERFACE,
		"struct" to FreeTokenType.STRUCT,
		"enum" to FreeTokenType.ENUM,
		"annotation" to FreeTokenType.ANNOTATION,
		"private" to FreeTokenType.PRIVATE,
		"file" to FreeTokenType.FILE,
		"internal" to FreeTokenType.INTERNAL,
		"module" to FreeTokenType.MODULE,
		"public" to FreeTokenType.PUBLIC,
		"const" to FreeTokenType.CONST,
		"open" to FreeTokenType.OPEN,
		"abstract" to FreeTokenType.ABSTRACT,
		"final" to FreeTokenType.FINAL,
		"override" to FreeTokenType.OVERRIDE,
		"ignore" to FreeTokenType.IGNORE,
		"only" to FreeTokenType.ONLY,
		"with" to FreeTokenType.WITH,
		"init" to FreeTokenType.INIT,
		"delete" to FreeTokenType.DELETE,
		"var" to FreeTokenType.VAR,
		"val" to FreeTokenType.VAL,
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
		"super" to FreeTokenType.SUPER,
		"true" to FreeTokenType.TRUE,
		"false" to FreeTokenType.FALSE,
		"null" to FreeTokenType.NULL,
	)
	
	private val keywords = keywordTokenTypeMap.keys
	
	context(_: FreeContext)
	override fun tryParse(input: CharArray, start: Int, line: Int, column: Int): FreeToken? {
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