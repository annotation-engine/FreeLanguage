package free.core.parser.declaration

import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.access
import free.core.parser.parameter.Parameter
import free.core.parser.parameter.parseStructParameters
import kotlinx.serialization.Serializable

@Serializable
data class StructDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val parameters: List<Parameter>
) : Declaration

class StructDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(modifiers: Set<Modifier>): StructDeclaration {
		ctx.expect(FreeTokenType.IDENTIFIER, "结构体缺少名称")
		val name = ctx.previous.value
		val structAccess = modifiers.access
		val parameters = parseStructParameters(ctx, structAccess)
		return StructDeclaration(
			name = name,
			modifiers = modifiers,
			parameters = parameters
		)
	}
}