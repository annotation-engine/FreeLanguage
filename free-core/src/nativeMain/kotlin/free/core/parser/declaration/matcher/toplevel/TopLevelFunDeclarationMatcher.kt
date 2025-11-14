package free.core.parser.declaration.matcher.toplevel

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.checkSupportedDeclarationModifiers
import free.core.parser.declaration.FunDeclaration
import free.core.parser.declaration.parser.FunDeclarationParser

object TopLevelFunDeclarationMatcher : TopLevelDeclarationMatcher<FunDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.FUN)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, "顶层函数",
			isSupportedConst = true
		)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, modifiers: Set<Modifier>): FunDeclaration {
		return FunDeclarationParser(ctx).parse(modifiers)
	}
}