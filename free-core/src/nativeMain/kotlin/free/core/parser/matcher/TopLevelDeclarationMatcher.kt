package free.core.parser.matcher

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.checkSupportedDeclarationModifiers
import free.core.parser.declaration.*

sealed interface TopLevelDeclarationMatcher<out D : Declaration> {
	
	val tokenType: FreeTokenType
	
	suspend fun checkAndParse(ctx: FreeParserContext, modifiers: Set<Modifier>): D
	
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
		
		suspend fun checkAndParse(ctx: FreeParserContext, modifiers: Set<Modifier>): Declaration {
			matchers.forEach {
				if (ctx.match(it.tokenType)) {
					return it.checkAndParse(ctx, modifiers)
				}
			}
			syntaxError("未知的顶层声明", ctx.current)
		}
	}
}

private object TopLevelFunDeclarationMatcher : TopLevelDeclarationMatcher<FunDeclaration> {
	
	override val tokenType = FreeTokenType.FUN
	
	override suspend fun checkAndParse(ctx: FreeParserContext, modifiers: Set<Modifier>): FunDeclaration {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, "顶层函数",
			isSupportedConst = true
		)
		return FunDeclarationParser(ctx).parse(modifiers)
	}
}

private object TopLevelClassDeclarationMatcher : TopLevelDeclarationMatcher<ClassDeclaration> {
	
	override val tokenType = FreeTokenType.CLASS
	
	override suspend fun checkAndParse(ctx: FreeParserContext, modifiers: Set<Modifier>): ClassDeclaration {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, "顶层类",
			isSupportedOpen = true,
			isSupportedAbstract = true
		)
		return ClassDeclarationParser(ctx).parse(modifiers)
	}
}

private object TopLevelSingleDeclarationMatcher : TopLevelDeclarationMatcher<SingleDeclaration> {
	
	override val tokenType = FreeTokenType.SINGLE
	
	override suspend fun checkAndParse(ctx: FreeParserContext, modifiers: Set<Modifier>): SingleDeclaration {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层单例类")
		return SingleDeclarationParser(ctx).parse(modifiers)
	}
}

private object TopLevelInterfaceDeclarationMatcher : TopLevelDeclarationMatcher<InterfaceDeclaration> {
	
	override val tokenType = FreeTokenType.INTERFACE
	
	override suspend fun checkAndParse(ctx: FreeParserContext, modifiers: Set<Modifier>): InterfaceDeclaration {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层接口")
		return InterfaceDeclarationParser(ctx).parse(modifiers)
	}
}

private object TopLevelStructDeclarationMatcher : TopLevelDeclarationMatcher<StructDeclaration> {
	
	override val tokenType = FreeTokenType.STRUCT
	
	override suspend fun checkAndParse(ctx: FreeParserContext, modifiers: Set<Modifier>): StructDeclaration {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层结构体")
		return StructDeclarationParser(ctx).parse(modifiers)
	}
}

private object TopLevelEnumDeclarationMatcher : TopLevelDeclarationMatcher<EnumDeclaration> {
	
	override val tokenType = FreeTokenType.ENUM
	
	override suspend fun checkAndParse(ctx: FreeParserContext, modifiers: Set<Modifier>): EnumDeclaration {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层枚举")
		return EnumDeclarationParser(ctx).parse(modifiers)
	}
}

private object TopLevelAnnotationDeclarationMatcher : TopLevelDeclarationMatcher<AnnotationDeclaration> {
	
	override val tokenType = FreeTokenType.ANNOTATION
	
	override suspend fun checkAndParse(ctx: FreeParserContext, modifiers: Set<Modifier>): AnnotationDeclaration {
		checkSupportedDeclarationModifiers(ctx, modifiers, "顶层注解")
		return AnnotationDeclarationParser(ctx).parse(modifiers)
	}
}