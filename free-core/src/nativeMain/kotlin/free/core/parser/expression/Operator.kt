package free.core.parser.expression

import free.core.lexer.FreeTokenType

enum class Operator(
	val priority: Int,
	val associativity: Associativity
) {
	DOUBLE_PLUS(
		priority = 12,
		associativity = Associativity.RIGHT,
	),
	DOUBLE_MINUS(
		priority = 12,
		associativity = Associativity.RIGHT,
	),
	NOT(
		priority = 11,
		associativity = Associativity.RIGHT,
	),
	BIT_NOT(
		priority = 11,
		associativity = Associativity.RIGHT,
	),
	DOUBLE_STAR(
		priority = 10,
		associativity = Associativity.RIGHT,
	),
	STAR(
		priority = 9,
		associativity = Associativity.LEFT,
	),
	SLASH(
		priority = 9,
		associativity = Associativity.LEFT,
	),
	PERCENT(
		priority = 9,
		associativity = Associativity.LEFT,
	),
	PLUS(
		priority = 8,
		associativity = Associativity.LEFT,
	),
	MINUS(
		priority = 8,
		associativity = Associativity.LEFT,
	),
	SHL(
		priority = 7,
		associativity = Associativity.LEFT,
	),
	SHR(
		priority = 7,
		associativity = Associativity.LEFT
	),
	USHR(
		priority = 7,
		associativity = Associativity.LEFT,
	),
	EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	NOT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	TRIPLE_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	TRIPLE_NOT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	GT(
		priority = 6,
		associativity = Associativity.NONE,
	),
	GT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	LT(
		priority = 6,
		associativity = Associativity.NONE,
	),
	LT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	IN(
		priority = 6,
		associativity = Associativity.NONE,
	),
	NOT_IN(
		priority = 6,
		associativity = Associativity.NONE,
	),
	BIT_AND(
		priority = 5,
		associativity = Associativity.LEFT,
	),
	BIT_XOR(
		priority = 4,
		associativity = Associativity.LEFT,
	),
	BIT_OR(
		priority = 3,
		associativity = Associativity.LEFT,
	),
	AND(
		priority = 2,
		associativity = Associativity.LEFT,
	),
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