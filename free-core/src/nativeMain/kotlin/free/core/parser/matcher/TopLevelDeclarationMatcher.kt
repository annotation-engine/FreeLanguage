package free.core.parser.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.checkSupportedDeclarationModifiers
import free.core.parser.declaration.*

sealed interface TopLevelDeclarationMatcher<out D : Declaration> {
	
	fun match(ctx: FreeParserContext): Boolean
	
	context(_: FreeContext)
	fun check(ctx: FreeParserContext, modifiers: Set<Modifier>)
	
	context(_: FreeContext)
	fun parse(ctx: FreeParserContext, modifiers: Set<Modifier>): D
	
	companion object {
		
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
		fun parse(ctx: FreeParserContext, modifiers: Set<Modifier>): Declaration {
			matchers.forEach { matcher ->
				if (matcher.match(ctx)) {
					matcher.check(ctx, modifiers)
					return matcher.parse(ctx, modifiers)
				}
			}
			syntaxError("未知的顶层声明", ctx.current)
		}
	}
}

private object TopLevelFunDeclarationMatcher : TopLevelDeclarationMatcher<FunDeclaration> {
	
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

private object TopLevelClassDeclarationMatcher : TopLevelDeclarationMatcher<ClassDeclaration> {
	
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

private object TopLevelSingleDeclarationMatcher : TopLevelDeclarationMatcher<SingleDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.SINGLE)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层单例类")
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, modifiers: Set<Modifier>): SingleDeclaration {
		return SingleDeclarationParser(ctx).parse(modifiers)
	}
}

private object TopLevelInterfaceDeclarationMatcher : TopLevelDeclarationMatcher<InterfaceDeclaration> {
	
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

private object TopLevelStructDeclarationMatcher : TopLevelDeclarationMatcher<StructDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.STRUCT)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层结构体")
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, modifiers: Set<Modifier>): StructDeclaration {
		return StructDeclarationParser(ctx).parse(modifiers)
	}
}

private object TopLevelEnumDeclarationMatcher : TopLevelDeclarationMatcher<EnumDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.ENUM)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层枚举")
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, modifiers: Set<Modifier>): EnumDeclaration {
		return EnumDeclarationParser(ctx).parse(modifiers)
	}
}

private object TopLevelAnnotationDeclarationMatcher : TopLevelDeclarationMatcher<AnnotationDeclaration> {
	
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