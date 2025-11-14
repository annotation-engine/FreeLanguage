package free.core.parser.statement

import free.core.parser.expression.Expression
import free.core.parser.node.TypeReference
import kotlinx.serialization.Serializable

@Serializable
data class VariableDeclarationStatement(
	val name: String,
	val initializer: Expression?,
	val isVariable: Boolean,
	val type: TypeReference?
) : Statement