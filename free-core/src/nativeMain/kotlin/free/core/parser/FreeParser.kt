package free.core.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType
import free.core.parser.declaration.Declaration
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
	
	private suspend fun parseDeclaration(): Declaration {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += getAccessModifier()
		when {
			ctx.match(FreeTokenType.OPEN) -> modifiers += Modifier.OPEN
			ctx.match(FreeTokenType.ABSTRACT) -> modifiers += Modifier.ABSTRACT
			ctx.match(FreeTokenType.OVERRIDE) -> modifiers += Modifier.OVERRIDE
			ctx.match(FreeTokenType.FINAL, FreeTokenType.OVERRIDE) -> {
				modifiers += Modifier.FINAL
				modifiers += Modifier.OVERRIDE
			}
		}
		return TopLevelDeclarationMatcher.parseAndCheck(ctx, modifiers)
	}
	
	private suspend fun getAccessModifier(): Modifier {
		return when {
			ctx.match(FreeTokenType.PRIVATE) -> Modifier.PRIVATE
			ctx.match(FreeTokenType.FILE) -> syntaxError("顶层函数不支持 'file' 修饰符", ctx.previous)
			ctx.match(FreeTokenType.INTERNAL) -> Modifier.INTERNAL
			ctx.match(FreeTokenType.MODULE) -> Modifier.MODULE
			ctx.match(FreeTokenType.PUBLIC) -> Modifier.PUBLIC
			else -> Modifier.PUBLIC
		}
	}
}