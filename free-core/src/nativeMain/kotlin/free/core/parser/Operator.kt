package free.core.parser

import free.core.lexer.FreeTokenType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Operator(
	val priority: Int,
	val associativity: Associativity
) {
	@SerialName("++")
	DOUBLE_PLUS(
		priority = 12,
		associativity = Associativity.RIGHT,
	),
	
	@SerialName("--")
	DOUBLE_MINUS(
		priority = 12,
		associativity = Associativity.RIGHT,
	),
	
	@SerialName("!")
	NOT(
		priority = 11,
		associativity = Associativity.RIGHT,
	),
	
	@SerialName("~")
	BIT_NOT(
		priority = 11,
		associativity = Associativity.RIGHT,
	),
	
	@SerialName("**")
	DOUBLE_STAR(
		priority = 10,
		associativity = Associativity.RIGHT,
	),
	
	@SerialName("*")
	STAR(
		priority = 9,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("/")
	SLASH(
		priority = 9,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("%")
	PERCENT(
		priority = 9,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("+")
	PLUS(
		priority = 8,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("-")
	MINUS(
		priority = 8,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("<<")
	SHL(
		priority = 7,
		associativity = Associativity.LEFT,
	),
	
	@SerialName(">>")
	SHR(
		priority = 7,
		associativity = Associativity.LEFT
	),
	
	@SerialName(">>>")
	USHR(
		priority = 7,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("==")
	EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("!=")
	NOT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("===")
	TRIPLE_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("!==")
	TRIPLE_NOT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName(">")
	GT(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName(">=")
	GT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("<")
	LT(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("<=")
	LT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("~>")
	IN(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("!>")
	NOT_IN(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("&")
	BIT_AND(
		priority = 5,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("^")
	BIT_XOR(
		priority = 4,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("|")
	BIT_OR(
		priority = 3,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("&&")
	AND(
		priority = 2,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("||")
	OR(
		priority = 1,
		associativity = Associativity.LEFT,
	)
}

enum class Associativity {
	LEFT,
	NONE,
	RIGHT
}

fun FreeTokenType.toOperator(): Operator = when (this) {
	FreeTokenType.DOUBLE_PLUS -> Operator.DOUBLE_PLUS
	FreeTokenType.DOUBLE_MINUS -> Operator.DOUBLE_MINUS
	FreeTokenType.NOT -> Operator.NOT
	FreeTokenType.BIT_NOT -> Operator.BIT_NOT
	FreeTokenType.DOUBLE_STAR -> Operator.DOUBLE_STAR
	FreeTokenType.STAR -> Operator.STAR
	FreeTokenType.SLASH -> Operator.SLASH
	FreeTokenType.PERCENT -> Operator.PERCENT
	FreeTokenType.PLUS -> Operator.PLUS
	FreeTokenType.MINUS -> Operator.MINUS
	FreeTokenType.SHL -> Operator.SHL
	FreeTokenType.SHR -> Operator.SHR
	FreeTokenType.USHR -> Operator.USHR
	FreeTokenType.EQUALS -> Operator.EQUALS
	FreeTokenType.NOT_EQUALS -> Operator.NOT_EQUALS
	FreeTokenType.TRIPLE_EQUALS -> Operator.TRIPLE_EQUALS
	FreeTokenType.TRIPLE_NOT_EQUALS -> Operator.TRIPLE_NOT_EQUALS
	FreeTokenType.GT -> Operator.GT
	FreeTokenType.GT_EQUALS -> Operator.GT_EQUALS
	FreeTokenType.LT -> Operator.LT
	FreeTokenType.LT_EQUALS -> Operator.LT_EQUALS
	FreeTokenType.IN -> Operator.IN
	FreeTokenType.NOT_IN -> Operator.NOT_IN
	FreeTokenType.BIT_AND -> Operator.BIT_AND
	FreeTokenType.BIT_XOR -> Operator.BIT_XOR
	FreeTokenType.BIT_OR -> Operator.BIT_OR
	FreeTokenType.AND -> Operator.AND
	FreeTokenType.OR -> Operator.OR
	else -> error("不支持的运算符")
}