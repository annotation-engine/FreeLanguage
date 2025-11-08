package free.core.parser.declaration

import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import kotlinx.serialization.Serializable

@Serializable
data class SingleDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val members: List<Declaration>
) : Declaration

class SingleDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	fun parse(modifiers: Set<Modifier>): SingleDeclaration {
		TODO()
	}
}