package free.core.parser.expression

import free.core.parser.Operator
import kotlinx.serialization.Serializable

@Serializable
data class SuffixUnaryExpression(
	val expression: Expression,
	val operator: Operator
) : Expression