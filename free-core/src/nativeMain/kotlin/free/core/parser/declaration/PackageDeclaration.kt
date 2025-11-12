package free.core.parser.declaration

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.node.AstNode
import kotlinx.serialization.Serializable

@Serializable
data class PackageDeclaration(
	val paths: List<String>,
) : AstNode

class PackageDeclarationParser(
	private val ctx: FreeParserContext,
) {
	
	suspend fun parse(): PackageDeclaration {
		if (ctx.match(FreeTokenType.PACKAGE)) {
			val packages = mutableListOf<String>()
			ctx.expect(FreeTokenType.IDENTIFIER, "package 后应跟包名")
			packages += ctx.previous.value
			
			while (ctx.match(FreeTokenType.DOT)) {
				ctx.expect(FreeTokenType.IDENTIFIER, "'.' 后应跟标识符")
				packages += ctx.previous.value
			}
			return PackageDeclaration(packages)
		}
		syntaxError("文件缺少包定义", ctx.current)
	}
}