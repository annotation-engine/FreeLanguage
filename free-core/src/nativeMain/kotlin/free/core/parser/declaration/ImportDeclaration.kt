package free.core.parser.declaration

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import kotlinx.serialization.Serializable

@Serializable
data class ImportDeclaration(
	val paths: List<String>,
	val alias: String?,
	val scope: ImportScope
) : Declaration

enum class ImportScope {
	SINGLE,
	WILDCARD,
	RECURSIVE
}

class ImportDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): ImportDeclaration {
		ctx.expect(FreeTokenType.IDENTIFIER, "import 缺少包名")
		val paths = mutableListOf(ctx.previous.value)
		var scope = ImportScope.SINGLE
		var alias: String? = null
		while (ctx.match(FreeTokenType.DOT)) {
			when {
				ctx.match(FreeTokenType.IDENTIFIER) -> {
					paths += ctx.previous.value
					if (ctx.match(FreeTokenType.AS)) {
						ctx.expect(FreeTokenType.IDENTIFIER, "as 后缺少别名")
						alias = ctx.previous.value
						break
					}
				}
				
				ctx.match(FreeTokenType.STAR) -> {
					scope = ImportScope.WILDCARD
					break
				}
				
				ctx.match(FreeTokenType.DOUBLE_STAR) -> {
					scope = ImportScope.RECURSIVE
					break
				}
			}
		}
		return ImportDeclaration(
			paths = paths,
			alias = alias,
			scope = scope
		)
	}
}