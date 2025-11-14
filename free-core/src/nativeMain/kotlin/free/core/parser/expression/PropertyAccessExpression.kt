package free.core.parser.expression

import kotlinx.serialization.Serializable

/**
 * 属性访问
 */
@Serializable
data class PropertyAccessExpression(
	val receiver: Expression?,
	val name: String
) : Expression