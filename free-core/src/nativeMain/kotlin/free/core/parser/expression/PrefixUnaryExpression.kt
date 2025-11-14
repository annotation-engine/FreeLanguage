package free.core.parser.expression

import free.core.parser.Operator
import kotlinx.serialization.Serializable

@Serializable
data class PrefixUnaryExpression(
	val operator: Operator,
	val expression: Expression
) : Expression