package free.core.parser.declaration

import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import kotlinx.serialization.Serializable

@Serializable
data class EnumDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val members: List<Declaration>
) : Declaration

class EnumDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	fun parse(modifiers: Set<Modifier>): EnumDeclaration {
		TODO()
	}
}