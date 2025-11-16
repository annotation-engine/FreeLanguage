package free.core.parser.declaration.matcher.toplevel

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.declaration.Declaration

sealed interface TopLevelDeclarationMatcher<out D : Declaration> {
	
	fun match(ctx: FreeParserContext): Boolean
	
	context(_: FreeContext)
	fun check(ctx: FreeParserContext, modifiers: Set<Modifier>)
	
	context(_: FreeContext)
	fun parse(ctx: FreeParserContext, modifiers: Set<Modifier>): D
}

private val matchers = listOf(
	TopLevelFunDeclarationMatcher,
	TopLevelClassDeclarationMatcher,
	TopLevelSingleDeclarationMatcher,
	TopLevelInterfaceDeclarationMatcher,
	TopLevelStructDeclarationMatcher,
	TopLevelEnumDeclarationMatcher,
	TopLevelAnnotationDeclarationMatcher
)

context(_: FreeContext)
fun parseTopLevelDeclaration(ctx: FreeParserContext, modifiers: Set<Modifier>): Declaration {
	val matcher = matchers.find { it.match(ctx) }
		?: syntaxError("未知的顶层声明", ctx.current)
	matcher.check(ctx, modifiers)
	return matcher.parse(ctx, modifiers)
}