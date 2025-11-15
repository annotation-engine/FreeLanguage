package free.core.parser.expression

import kotlinx.serialization.Serializable

@Serializable
data class CallExpression(
	val callee: Expression,
	val arguments: List<Expression> = emptyList()
) : Expression