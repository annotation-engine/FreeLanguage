package free.core.parser.declaration

import free.core.parser.Modifier
import kotlinx.serialization.Serializable

@Serializable
data class SingleDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val members: List<Declaration> = emptyList(),
) : Declaration