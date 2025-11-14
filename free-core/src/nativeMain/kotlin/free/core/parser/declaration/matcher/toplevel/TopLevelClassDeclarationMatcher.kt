package free.core.parser.declaration.matcher.toplevel

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.checkSupportedDeclarationModifiers
import free.core.parser.declaration.ClassDeclaration
import free.core.parser.declaration.parser.ClassDeclarationParser

object TopLevelClassDeclarationMatcher : TopLevelDeclarationMatcher<ClassDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.CLASS)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, "顶层类",
			isSupportedOpen = true,
			isSupportedAbstract = true
		)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, modifiers: Set<Modifier>): ClassDeclaration {
		return ClassDeclarationParser(ctx).parse(modifiers)
	}
}