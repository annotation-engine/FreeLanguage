package free.core.parser.expression

import free.core.parser.FreeParserContext
import kotlinx.serialization.Serializable

/**
 * 属性访问
 */
@Serializable
data class PropertyAccessExpression(
	val receiver: Expression?,
	val name: String
) : Expression

class PropertyAccessExpressionParser(
	private val ctx: FreeParserContext
) {
	
	fun parse(): PropertyAccessExpression {
		TODO()
	}
}