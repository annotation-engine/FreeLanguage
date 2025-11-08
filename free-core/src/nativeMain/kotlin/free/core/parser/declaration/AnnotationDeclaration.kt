package free.core.parser.declaration

import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import kotlinx.serialization.Serializable

@Serializable
data class AnnotationDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val members: List<Declaration>
) : Declaration

class AnnotationDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	fun parse(modifiers: Set<Modifier>): AnnotationDeclaration {
		TODO()
	}
}