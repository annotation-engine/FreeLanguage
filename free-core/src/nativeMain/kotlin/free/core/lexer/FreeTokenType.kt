package free.core.lexer

enum class FreeTokenType {
	FUN, CLASS, SINGLE, INTERFACE, STRUCT, ENUM, ANNOTATION,                        // class single interface enum annotation struct fun
	PRIVATE, FILE, INTERNAL, MODULE, PUBLIC,                                        // public module internal file private
	CONST, OPEN, ABSTRACT, FINAL, OVERRIDE, STRUCTLESS,                             // const open abstract final override structless
	ONLY, WITH, INIT, DELETE,                                                       // only with init delete
	VAR, VAL,                                                                       // var val
	IF, ELSE, MATCH,                                                                // if else match
	FOR, WHILE, DO,                                                                 // for while do while
	RETURN, BREAK, CONTINUE,                                                        // return break continue
	AS, IS,                                                                         // as is
	PACKAGE, IMPORT, THIS, SUPER, MUT,                                              // package import this super mut
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