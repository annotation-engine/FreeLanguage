package free.core.parser.matcher

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.*
import free.core.parser.declaration.*

sealed interface MemberDeclarationMatcher<out D : Declaration> {
	
	val tokenType: FreeTokenType
	
	suspend fun checkAndParse(
		ctx: FreeParserContext,
		typeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		memberModifiers: Set<Modifier>
	): D
	
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
		
		suspend fun checkAndParse(
			ctx: FreeParserContext,
			typeKind: TypeKind,
			parentModifiers: Set<Modifier>,
			memberModifiers: Set<Modifier>
		): Declaration {
			matchers.forEach {
				if (ctx.match(it.tokenType)) {
					return it.checkAndParse(ctx, typeKind, parentModifiers, memberModifiers)
				}
			}
			syntaxError("未知的成员声明", ctx.current)
		}
	}
}

private object MemberFunDeclarationMatcher : MemberDeclarationMatcher<FunDeclaration> {
	
	override val tokenType = FreeTokenType.FUN
	
	override suspend fun checkAndParse(
		ctx: FreeParserContext,
		typeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		memberModifiers: Set<Modifier>
	): FunDeclaration {
		if (typeKind == TypeKind.ENUM_ENTRY) {
			checkSupportedDeclarationModifiers(
				ctx, memberModifiers, name = "枚举常量成员函数",
				isSupportedOverride = true
			)
		} else {
			checkSupportedDeclarationModifiers(
				ctx, memberModifiers, name = "成员函数",
				isSupportedOpen = parentModifiers.isOpen,
				isSupportedAbstract = parentModifiers.isAbstract,
				isSupportedFinalOverride = parentModifiers.isAbstract,
				isSupportedOverride = parentModifiers.isAbstract,
			)
		}
		return FunDeclarationParser(ctx).parse(memberModifiers)
	}
}

private object MemberClassDeclarationMatcher : MemberDeclarationMatcher<ClassDeclaration> {
	
	override val tokenType = FreeTokenType.CLASS
	
	override suspend fun checkAndParse(
		ctx: FreeParserContext,
		typeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		memberModifiers: Set<Modifier>
	): ClassDeclaration {
		checkSupportedDeclarationModifiers(
			ctx, memberModifiers, name = "内部类",
			isSupportedOpen = true,
			isSupportedAbstract = true
		)
		return ClassDeclarationParser(ctx).parse(memberModifiers)
	}
}

private object MemberSingleDeclarationMatcher : MemberDeclarationMatcher<SingleDeclaration> {
	
	override val tokenType = FreeTokenType.SINGLE
	
	override suspend fun checkAndParse(
		ctx: FreeParserContext,
		typeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		memberModifiers: Set<Modifier>
	): SingleDeclaration {
		checkSupportedDeclarationModifiers(
			ctx, memberModifiers, name = "单例类"
		)
		return SingleDeclarationParser(ctx).parse(memberModifiers, typeKind)
	}
}

private object MemberInterfaceDeclarationMatcher : MemberDeclarationMatcher<InterfaceDeclaration> {
	
	override val tokenType = FreeTokenType.INTERFACE
	
	override suspend fun checkAndParse(
		ctx: FreeParserContext,
		typeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		memberModifiers: Set<Modifier>
	): InterfaceDeclaration {
		checkSupportedDeclarationModifiers(
			ctx, memberModifiers, name = "接口",
		)
		return InterfaceDeclarationParser(ctx).parse(memberModifiers)
	}
}

private object MemberStructDeclarationMatcher : MemberDeclarationMatcher<StructDeclaration> {
	
	override val tokenType = FreeTokenType.STRUCT
	
	override suspend fun checkAndParse(
		ctx: FreeParserContext,
		typeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		memberModifiers: Set<Modifier>
	): StructDeclaration {
		checkSupportedDeclarationModifiers(
			ctx, memberModifiers, name = "结构体",
		)
		return StructDeclarationParser(ctx).parse(memberModifiers)
	}
}

private object MemberEnumDeclarationMatcher : MemberDeclarationMatcher<EnumDeclaration> {
	
	override val tokenType = FreeTokenType.ENUM
	
	override suspend fun checkAndParse(
		ctx: FreeParserContext,
		typeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		memberModifiers: Set<Modifier>
	): EnumDeclaration {
		checkSupportedDeclarationModifiers(
			ctx, memberModifiers, name = "枚举",
		)
		return EnumDeclarationParser(ctx).parse(memberModifiers)
	}
}

private object MemberAnnotationDeclarationMatcher : MemberDeclarationMatcher<AnnotationDeclaration> {
	
	override val tokenType = FreeTokenType.ANNOTATION
	
	override suspend fun checkAndParse(
		ctx: FreeParserContext,
		typeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		memberModifiers: Set<Modifier>
	): AnnotationDeclaration {
		checkSupportedDeclarationModifiers(
			ctx, memberModifiers, name = "注解",
		)
		return AnnotationDeclarationParser(ctx).parse(memberModifiers)
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