package free.core.parser.declaration

import free.core.parser.Modifier
import free.core.parser.node.TypeReference
import free.core.parser.parameter.Parameter
import free.core.parser.statement.Statement
import kotlinx.serialization.Serializable

@Serializable
data class FunDeclaration(
	val name: String,
	val parameters: List<Parameter>,
	val modifiers: Set<Modifier>,
	val returnTypes: List<TypeReference>,
	val statements: List<Statement> = emptyList(),
) : Declaration