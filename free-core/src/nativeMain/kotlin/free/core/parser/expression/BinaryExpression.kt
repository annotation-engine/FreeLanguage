package free.core.parser.expression

import free.core.parser.Operator
import kotlinx.serialization.Serializable

@Serializable
data class BinaryExpression(
	val left: Expression,
	val operator: Operator,
	val right: Expression,
) : Expression