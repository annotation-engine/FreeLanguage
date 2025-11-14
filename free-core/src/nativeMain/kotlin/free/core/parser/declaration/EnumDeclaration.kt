package free.core.parser.declaration

import free.core.parser.Modifier
import free.core.parser.parameter.Parameter
import kotlinx.serialization.Serializable

@Serializable
data class EnumDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val parameters: List<Parameter>,
	val entries: List<EnumEntry>,
	val members: List<Declaration> = emptyList(),
) : Declaration

@Serializable
data class EnumEntry(
	val name: String,
	val members: List<Declaration>,
)