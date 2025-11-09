package free.core.parser.declaration

import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.node.Parameter
import kotlinx.serialization.Serializable

@Serializable
data class StructDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val constructorModifiers: Set<Modifier>,
	val parameters: List<Parameter>
) : Declaration

class StructDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	fun parse(modifiers: Set<Modifier>): StructDeclaration {
	
	}
}