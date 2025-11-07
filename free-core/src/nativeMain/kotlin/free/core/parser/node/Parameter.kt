package free.core.parser.node

import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import kotlinx.serialization.Serializable

@Serializable
data class Parameter(
	val name: String,
	val modifiers: List<Modifier>,
	val typeReference: TypeReference,
)

class ParameterParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parseWhenLambda(): Parameter {
		val name = if (ctx.peek(offset = 1)?.type == FreeTokenType.COLON) {
			ctx.expect(FreeTokenType.IDENTIFIER, "参数缺少名称")
			ctx.previous.value.also {
				ctx.expect(FreeTokenType.COLON, "函数缺少 ':'")
			}
		} else ""
		val typeReference = TypeReferenceParser(ctx).parse()
		return Parameter(
			name = name,
			modifiers = emptyList(),
			typeReference = typeReference,
		)
	}
}