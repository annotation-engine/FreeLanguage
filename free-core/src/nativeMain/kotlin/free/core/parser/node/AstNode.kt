package free.core.parser.node

import free.core.parser.declaration.Declaration
import free.core.parser.declaration.PackageDeclaration
import kotlinx.serialization.Serializable

interface AstNode

@Serializable
data class Program(
	val files: List<SourceFileNode>
)

@Serializable
data class SourceFileNode(
	val path: String,
	val packageDeclaration: PackageDeclaration,
	val declarations: List<Declaration>
) : AstNode