package free.core.parser.parameter

import free.core.parser.Modifier
import free.core.parser.node.TypeReference
import kotlinx.serialization.Serializable

@Serializable
data class Parameter(
	val name: String,
	val modifiers: Set<Modifier>,
	val typeReference: TypeReference,
)