package free.core.lexer

enum class FreeTokenType {
	// ==============================
	// 关键字
	// ==============================
	FUN,                // fun
	CLASS,              // class
	SINGLE,             // single
	INTERFACE,          // interface
	STRUCT,             // struct
	ENUM,               // enum
	ANNOTATION,         // annotation
	PRIVATE,            // private
	FILE,               // file
	INTERNAL,           // internal
	MODULE,             // module
	PUBLIC,             // public
	OPEN,               // open
	ABSTRACT,           // abstract
	FINAL,              // final
	OVERRIDE,           // override
	CONST,              // const
	IGNORE,             // ignore
	ONLY,               // only
	WITH,               // with
	INIT,               // init
	DELETE,             // delete
	VAR,                // var
	VAL,                // val
	IF,                 // if
	ELSE,               // else
	MATCH,              // match
	FOR,                // for
	WHILE,              // while
	DO,                 // do
	LOOP,               // loop
	RETURN,             // return
	BREAK,              // break
	CONTINUE,           // continue
	AS,                 // as
	IS,                 // is
	PACKAGE,            // package
	IMPORT,             // import
	THIS,               // this
	SUPER,              // super
	TRUE,               // true
	FALSE,              // false
	NULL,               // null
	
	// ==============================
	// 标识符
	// ==============================
	IDENTIFIER,         // 标识符
	
	// ==============================
	// 字面量
	// ==============================
	STRING,             // 字符串
	CHAR,               // 字符
	NUMBER,             // 数字
	
	// ==============================
	// 一元运算符
	// ==============================
	PLUS,               // +
	MINUS,              // -
	BANG,               // !
	BIT_NOT,            // ~
	DOUBLE_PLUS,        // ++
	DOUBLE_MINUS,       // --
	
	// ==============================
	// 二元运算符
	// ==============================
	STAR,               // *
	SLASH,              // /
	PERCENT,            // %
	DOUBLE_STAR,        // **
	EQUALS,             // ==
	NOT_EQUALS,         // !=
	GT,                 // >
	GT_EQUALS,          // >=
	LT,                 // <
	LT_EQUALS,          // <=
	TRIPLE_EQUALS,      // ===
	TRIPLE_NOT_EQUALS,  // !==
	IN,                 // ~>
	NOT_IN,             // !>
	BIT_AND,            // &
	BIT_OR,             // |
	BIT_XOR,            // ^
	SHL,                // <<
	SHR,                // >>
	USHR,               // >>>
	
	// ==============================
	// 逻辑运算符
	// ==============================
	AND,                // &&
	OR,                 // ||
	
	// ==============================
	// 三元运算符
	// ==============================
	COLON,              // :
	QUESTION,           // ?
	ELVIS,              // ?:
	
	// ==============================
	// 赋值运算符
	// ==============================
	ASSIGN,             // =
	QUESTION_ASSIGN,    // ?=
	PLUS_ASSIGN,        // +=
	MINUS_ASSIGN,       // -=
	STAR_ASSIGN,        // *=
	SLASH_ASSIGN,       // /=
	PERCENT_ASSIGN,     // %=
	
	// ==============================
	// 成员访问
	// ==============================
	DOT,                // .
	QUESTION_DOT,       // ?.
	DOUBLE_COLON,       // ::
	LPAREN,             // (
	RPAREN,             // )
	LBRACKET,           // [
	RBRACKET,           // ]
	
	// ==============================
	// 分隔符
	// ==============================
	
	COMMA,              // ,
	SEMICOLON,          // ;
	LBRACE,             // {
	RBRACE,             // }
	
	// ==============================
	// 特殊
	// ==============================
	AT,                 // @
	ARROW,              // ->
	DOUBLE_DOT,         // ..
	COMMENT,            // #
	NEWLINE,            // 换行符
	WHITE_SPACE,        // 空格
	TAB,                // 制表符
	EOF,                // 结束符
}