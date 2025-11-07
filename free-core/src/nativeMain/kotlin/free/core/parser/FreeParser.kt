package free.core.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType
import free.core.parser.declaration.ClassDeclarationParser
import free.core.parser.declaration.Declaration
import free.core.parser.declaration.FunDeclarationParser
import free.core.parser.declaration.PackageDeclarationParser
import free.core.parser.node.SourceFileNode
import kotlinx.coroutines.currentCoroutineContext

class FreeParser(
	rawTokens: List<FreeToken>
) {
	
	private val ctx = FreeParserContext(rawTokens)
	
	suspend fun parse(): SourceFileNode {
		val sourcePath = currentCoroutineContext()[FreeContext]!!.sourcePath
		val packageDeclaration = PackageDeclarationParser(ctx).parse()
		val declarations = mutableListOf<Declaration>()
		while (!ctx.isAtEnd()) {
			declarations += parseDeclaration()
		}
		return SourceFileNode(
			path = sourcePath,
			packageDeclaration = packageDeclaration,
			declarations = declarations,
		)
	}
	
	private suspend fun parseDeclaration(): Declaration = when {
		ctx.match(FreeTokenType.PRIVATE) -> parseDeclaration(Modifier.PRIVATE)
		ctx.match(FreeTokenType.FILE) -> syntaxError("顶层函数不支持 file 访问修饰符", ctx.previous)
		ctx.match(FreeTokenType.INTERNAL) -> parseDeclaration(Modifier.LOCAL)
		ctx.match(FreeTokenType.MODULE) -> parseDeclaration(Modifier.MODULE)
		ctx.match(FreeTokenType.PUBLIC) -> parseDeclaration(Modifier.PUBLIC)
		else -> parseDeclaration(Modifier.PUBLIC)
	}
	
	private suspend fun parseDeclaration(access: Modifier): Declaration = when {
		ctx.match(FreeTokenType.FUN) -> FunDeclarationParser(ctx).parse(access)
		ctx.match(FreeTokenType.CLASS) -> ClassDeclarationParser(ctx).parse(access)
		else -> syntaxError("未知的顶层声明: ", ctx.current)
	}
}