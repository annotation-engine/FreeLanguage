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

class LambdaParameterParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): Parameter {
		val name = if (ctx.peek(offset = 1)?.type == FreeTokenType.COLON) {
			ctx.expect(FreeTokenType.IDENTIFIER, "参数缺少名称")
			ctx.previous.value.also {
				ctx.expect(FreeTokenType.COLON, "参数缺少 ':'")
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

class FunParameterParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): Parameter {
		val modifiers = listOf(
			when {
				ctx.match(FreeTokenType.VAR) -> Modifier.VAR
				else -> Modifier.VAL
			}
		)
		ctx.expect(FreeTokenType.IDENTIFIER, "参数缺少名称")
		val name = ctx.previous.value
		ctx.expect(FreeTokenType.COLON, "参数缺少 ':'")
		val typeReference = TypeReferenceParser(ctx).parse()
		return Parameter(
			name = name,
			modifiers = modifiers,
			typeReference = typeReference
		)
	}
}