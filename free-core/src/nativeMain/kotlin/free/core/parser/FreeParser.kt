package free.core.parser

import free.core.FreeContext
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType
import free.core.parser.declaration.Declaration
import free.core.parser.declaration.ImportDeclaration
import free.core.parser.declaration.ImportDeclarationParser
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
		val importDeclarations = mutableListOf<ImportDeclaration>()
		while (ctx.match(FreeTokenType.IMPORT)) {
			importDeclarations += ImportDeclarationParser(ctx).parse()
		}
		val declarations = mutableListOf<Declaration>()
		while (!ctx.isAtEnd()) {
			declarations += parseDeclaration()
		}
		return SourceFileNode(
			path = sourcePath,
			packageDeclaration = packageDeclaration,
			importDeclarations = importDeclarations,
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