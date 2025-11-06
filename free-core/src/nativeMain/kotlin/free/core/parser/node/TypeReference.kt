package free.core.parser.node

import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import kotlinx.serialization.Serializable

@Serializable
data class TypeReference(
	val types: String,
	val isNullable: Boolean = false,
	val isArray: Boolean = false
)

class TypeReferenceParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): TypeReference {
		ctx.expect(FreeTokenType.IDENTIFIER, "无法识别标识符")
		val type = mutableListOf<String>()
		var isNullable = false
		var isArray = false
		type += ctx.previous.value
		while (ctx.match(FreeTokenType.DOT)) {
			ctx.expect(FreeTokenType.IDENTIFIER, "无法识别标识符")
			type += ctx.previous.value
		}
		if (ctx.match(FreeTokenType.LBRACKET)) {
			ctx.expect(FreeTokenType.RBRACKET, "数组类型缺少 ']'")
			isArray = true
		}
		if (ctx.match(FreeTokenType.QUESTION)) {
			isNullable = true
		}
		return TypeReference(
			types = type.joinToString("."),
			isNullable = isNullable,
			isArray = isArray
		)
	}
}