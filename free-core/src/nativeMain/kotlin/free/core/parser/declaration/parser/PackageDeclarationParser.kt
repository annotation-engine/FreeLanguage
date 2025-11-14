package free.core.parser.declaration.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.declaration.PackageDeclaration

class PackageDeclarationParser(
	private val ctx: FreeParserContext,
) {
	
	context(_: FreeContext)
	fun parse(): PackageDeclaration {
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