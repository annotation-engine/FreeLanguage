package free.core.parser.declaration.matcher.toplevel

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.checkSupportedDeclarationModifiers
import free.core.parser.declaration.AnnotationDeclaration
import free.core.parser.declaration.parser.AnnotationDeclarationParser

object TopLevelAnnotationDeclarationMatcher : TopLevelDeclarationMatcher<AnnotationDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.ANNOTATION)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层注解")
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, modifiers: Set<Modifier>): AnnotationDeclaration {
		return AnnotationDeclarationParser(ctx).parse(modifiers)
	}
}