package free.core.parser

import free.core.FreeContext
import free.core.lexer.FreeToken
import free.core.parser.declaration.Declaration
import free.core.parser.declaration.PackageDeclarationParser
import free.core.parser.matcher.TopLevelDeclarationMatcher
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
		modifiers += getTopLevelAccessModifier(ctx)
		modifiers += getDeclarationModifiers(ctx)
		return TopLevelDeclarationMatcher.checkAndParse(ctx, modifiers)
	}
}