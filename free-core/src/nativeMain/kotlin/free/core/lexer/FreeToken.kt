package free.core.lexer

import free.core.FreeContext
import kotlinx.coroutines.currentCoroutineContext

class FreeToken(
	val type: FreeTokenType,
	val value: String,
	val start: Int,
	val end: Int,
	val line: Int,
	val column: Int,
) {
	
	val length: Int
		get() = this.end - this.start
}

suspend fun List<FreeToken>.formatToString(): String {
	val sourcePath = currentCoroutineContext()[FreeContext]!!.sourcePath
	val lineColumns = this.map { "$sourcePath:${it.line}:${it.column}" }
	val max = lineColumns.maxOf { it.length }
	val tokens = this
	return buildString {
		tokens.forEachIndexed { index, token ->
			val lineColumn = lineColumns[index]
			append(lineColumn)
			append(" ".repeat(max - lineColumn.length + 2))
			if (token.type == FreeTokenType.STRING || token.value.isNotEmpty()) {
				append("${token.type}(\"${token.value}\")")
			} else {
				append(token.type)
			}
			append("\n")
		}
	}
}

enum class FreeTokenType {
	CLASS, SINGLE, INTERFACE, ENUM, ANNOTATION, STRUCT, SERIAL,                     // class single interface enum annotation struct serial
	PUBLIC, MODULE, LOCAL, FILE, PRIVATE,                                           // public module local file private
	OPEN, ABSTRACT, ONLY, FINAL, OVERRIDE, SUPER, WITH, INIT, DELETE,               // open abstract only final override super with init delete
	VAR, VAL, CONST,                                                                // var val const
	FUN,                                                                            // fun
	IF, ELSE, MATCH,                                                                // if else match
	FOR, WHILE, DO,                                                                 // for while do while
	RETURN, BREAK, CONTINUE,                                                        // return break continue
	AS, IS,                                                                         // as is
	PACKAGE, IMPORT, THIS, MUT,                                                     // package import this mut
	TRUE, FALSE, NULL,                                                              // true false null
	NEWLINE, EOF, WHITE_SPACE, TAB,                                                 // \n EOF ' ' '\t'
	IDENTIFIER, STRING, CHAR, NUMBER,                                               // 标识符 字符串 字符 数字
	PLUS, MINUS, STAR, SLASH, PERCENT, DOUBLE_STAR,                                 // + - * / % **
	DOUBLE_PLUS, DOUBLE_MINUS,                                                      // ++ --
	ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, STAR_ASSIGN, SLASH_ASSIGN, PERCENT_ASSIGN,   // = += -= *= /= %=
	EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS, TRIPLE_EQUAL,                 // == != > >= < <= ===
	AND, OR, NOT,                                                                   // && || !
	BIT_AND, BIT_OR, BIT_XOR, BIT_NOT, SHL, SHR, USHR,                              // & | ^ ~ << >> >>>
	DOT, COMMA, SEMICOLON, COLON, DOUBLE_COLON, QUESTION, AT, COMMENT,              // . , ; : :: ? @ #
	IN, NOT_IN,                                                                     // ~> !～>
	ELVIS, ARROW, DOUBLE_DOT, QUESTION_ASSIGN, QUESTION_DOT, NOT_NULL_ASSERT,       // ?: -> .. ?= ?. !!
	LPAREN, RPAREN, LBRACKET, RBRACKET, LBRACE, RBRACE,                             // ( ) [ ] { }
}