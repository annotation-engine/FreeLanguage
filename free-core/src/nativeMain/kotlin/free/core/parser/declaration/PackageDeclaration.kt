package free.core.parser.declaration

import free.core.parser.node.AstNode
import kotlinx.serialization.Serializable

@Serializable
data class PackageDeclaration(
	val paths: List<String>,
) : AstNode