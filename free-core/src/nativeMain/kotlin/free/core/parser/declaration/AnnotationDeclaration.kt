package free.core.parser.declaration

import free.core.parser.Modifier
import free.core.parser.parameter.Parameter
import kotlinx.serialization.Serializable

@Serializable
data class AnnotationDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val parameters: List<Parameter> = emptyList(),
) : Declaration