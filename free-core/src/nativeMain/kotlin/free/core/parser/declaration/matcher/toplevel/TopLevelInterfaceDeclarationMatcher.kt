package free.core.parser.declaration.matcher.toplevel

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.checkSupportedDeclarationModifiers
import free.core.parser.declaration.InterfaceDeclaration
import free.core.parser.declaration.parser.InterfaceDeclarationParser

object TopLevelInterfaceDeclarationMatcher : TopLevelDeclarationMatcher<InterfaceDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.INTERFACE)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层接口")
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, modifiers: Set<Modifier>): InterfaceDeclaration {
		return InterfaceDeclarationParser(ctx).parse(modifiers)
	}
}