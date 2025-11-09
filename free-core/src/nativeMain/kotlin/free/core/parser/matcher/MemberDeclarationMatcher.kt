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
	
	companion object Companion {
		
		private val matchers = listOf(
			MemberFunDeclarationMatcher,
			MemberClassDeclarationMatcher,
			MemberSingleDeclarationMatcher
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
		checkSupportedDeclarationModifiers(
			ctx, memberModifiers, name = "成员函数",
			isSupportedOpen = parentModifiers.isOpen,
			isSupportedAbstract = parentModifiers.isAbstract,
			isSupportedFinalOverride = parentModifiers.isAbstract,
			isSupportedOverride = parentModifiers.isAbstract,
		)
		return FunDeclarationParser(ctx).parse(memberModifiers)
	}
}

private object MemberClassDeclarationMatcher : MemberDeclarationMatcher<FunDeclaration> {
	
	override val tokenType = FreeTokenType.CLASS
	
	override suspend fun checkAndParse(
		ctx: FreeParserContext,
		typeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		memberModifiers: Set<Modifier>
	): FunDeclaration {
		checkSupportedDeclarationModifiers(
			ctx, memberModifiers, name = "内部类",
			isSupportedOpen = true,
			isSupportedAbstract = true
		)
		return FunDeclarationParser(ctx).parse(memberModifiers)
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

enum class TypeKind {
	CLASS,
	SINGLE,
	INTERFACE,
	STRUCT,
	ENUM,
	ANNOTATION
}