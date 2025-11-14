package free.core.parser.matcher

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.*
import free.core.parser.declaration.*

sealed interface MemberDeclarationMatcher<out D : Declaration> {
	
	fun match(ctx: FreeParserContext): Boolean
	
	context(_: FreeContext)
	fun check(ctx: FreeParserContext, parentTypeKind: TypeKind, parentModifiers: Set<Modifier>, modifiers: Set<Modifier>)
	
	context(_: FreeContext)
	fun parse(ctx: FreeParserContext, parentTypeKind: TypeKind, modifiers: Set<Modifier>): D
	
	companion object {
		
		private val matchers = listOf(
			MemberFunDeclarationMatcher,
			MemberClassDeclarationMatcher,
			MemberSingleDeclarationMatcher,
			MemberInterfaceDeclarationMatcher,
			MemberStructDeclarationMatcher,
			MemberEnumDeclarationMatcher,
			MemberAnnotationDeclarationMatcher
		)
		
		context(_: FreeContext)
		fun parse(
			ctx: FreeParserContext,
			parentTypeKind: TypeKind,
			parentModifiers: Set<Modifier>,
			modifiers: Set<Modifier>
		): Declaration {
			matchers.forEach { matcher ->
				if (matcher.match(ctx)) {
					matcher.check(ctx, parentTypeKind, parentModifiers, modifiers)
					return matcher.parse(ctx, parentTypeKind, modifiers)
				}
			}
			syntaxError("未知的成员声明", ctx.current)
		}
	}
}

private object MemberFunDeclarationMatcher : MemberDeclarationMatcher<FunDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.FUN)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, parentTypeKind: TypeKind, parentModifiers: Set<Modifier>, modifiers: Set<Modifier>) {
		when (parentTypeKind) {
			TypeKind.ENUM -> checkSupportedDeclarationModifiers(
				ctx, modifiers, name = "枚举成员函数",
				isSupportedOpen = true,
				isSupportedAbstract = true,
			)
			
			TypeKind.ENUM_ENTRY -> checkSupportedDeclarationModifiers(
				ctx, modifiers, name = "枚举常量成员函数",
				isSupportedOverride = true
			)
			
			else -> checkSupportedDeclarationModifiers(
				ctx, modifiers, name = "成员函数",
				isSupportedOpen = parentModifiers.isOpen,
				isSupportedAbstract = parentModifiers.isAbstract,
				isSupportedFinalOverride = parentModifiers.isAbstract,
				isSupportedOverride = parentModifiers.isAbstract,
			)
		}
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, parentTypeKind: TypeKind, modifiers: Set<Modifier>): FunDeclaration {
		return FunDeclarationParser(ctx).parse(modifiers)
	}
}

private object MemberClassDeclarationMatcher : MemberDeclarationMatcher<ClassDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.CLASS)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, parentTypeKind: TypeKind, parentModifiers: Set<Modifier>, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, name = "内部类",
			isSupportedOpen = true,
			isSupportedAbstract = true
		)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, parentTypeKind: TypeKind, modifiers: Set<Modifier>): ClassDeclaration {
		return ClassDeclarationParser(ctx).parse(modifiers)
	}
}

private object MemberSingleDeclarationMatcher : MemberDeclarationMatcher<SingleDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.SINGLE)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, parentTypeKind: TypeKind, parentModifiers: Set<Modifier>, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, name = "单例类"
		)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, parentTypeKind: TypeKind, modifiers: Set<Modifier>): SingleDeclaration {
		return SingleDeclarationParser(ctx).parse(modifiers, parentTypeKind)
	}
}

private object MemberInterfaceDeclarationMatcher : MemberDeclarationMatcher<InterfaceDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.INTERFACE)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, parentTypeKind: TypeKind, parentModifiers: Set<Modifier>, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, name = "接口",
		)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, parentTypeKind: TypeKind, modifiers: Set<Modifier>): InterfaceDeclaration {
		return InterfaceDeclarationParser(ctx).parse(modifiers)
	}
}

private object MemberStructDeclarationMatcher : MemberDeclarationMatcher<StructDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.STRUCT)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, parentTypeKind: TypeKind, parentModifiers: Set<Modifier>, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, name = "结构体",
		)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, parentTypeKind: TypeKind, modifiers: Set<Modifier>): StructDeclaration {
		return StructDeclarationParser(ctx).parse(modifiers)
	}
}

private object MemberEnumDeclarationMatcher : MemberDeclarationMatcher<EnumDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.ENUM)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, parentTypeKind: TypeKind, parentModifiers: Set<Modifier>, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, name = "枚举",
		)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, parentTypeKind: TypeKind, modifiers: Set<Modifier>): EnumDeclaration {
		return EnumDeclarationParser(ctx).parse(modifiers)
	}
}

private object MemberAnnotationDeclarationMatcher : MemberDeclarationMatcher<AnnotationDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.ANNOTATION)
	}
	
	context(_: FreeContext)
	override fun check(ctx: FreeParserContext, parentTypeKind: TypeKind, parentModifiers: Set<Modifier>, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, name = "注解",
		)
	}
	
	context(_: FreeContext)
	override fun parse(ctx: FreeParserContext, parentTypeKind: TypeKind, modifiers: Set<Modifier>): AnnotationDeclaration {
		return AnnotationDeclarationParser(ctx).parse(modifiers)
	}
}

enum class TypeKind {
	CLASS,
	SINGLE,
	INTERFACE,
	STRUCT,
	ENUM,
	ENUM_ENTRY,
	ANNOTATION
}