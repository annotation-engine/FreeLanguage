package free.core.parser

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.declaration.*

sealed interface TopLevelDeclarationMatcher<out D : Declaration> {
	
	val tokenType: FreeTokenType
	
	suspend fun parseAndCheck(ctx: FreeParserContext, modifiers: Set<Modifier>): D
	
	companion object {
		
		private val matchers = listOf(
			FunDeclarationMatcher,
			ClassDeclarationMatcher,
			SingleDeclarationMatcher,
			InterfaceDeclarationMatcher,
			StructDeclarationMatcher,
			EnumDeclarationMatcher,
			AnnotationDeclarationMatcher
		)
		
		suspend fun parseAndCheck(ctx: FreeParserContext, modifiers: Set<Modifier>): Declaration {
			matchers.forEach {
				if (ctx.match(it.tokenType)) {
					return it.parseAndCheck(ctx, modifiers)
				}
			}
			syntaxError("未知的顶层声明", ctx.current)
		}
	}
}

private object FunDeclarationMatcher : TopLevelDeclarationMatcher<FunDeclaration> {
	
	override val tokenType = FreeTokenType.FUN
	
	override suspend fun parseAndCheck(ctx: FreeParserContext, modifiers: Set<Modifier>): FunDeclaration {
		when {
			Modifier.OPEN in modifiers -> syntaxError("顶层函数不支持 'open' 修饰符", ctx.previous)
			Modifier.ABSTRACT in modifiers -> syntaxError("顶层函数不支持 'abstract' 修饰符", ctx.previous)
			Modifier.OVERRIDE in modifiers -> syntaxError("顶层函数不支持 'override' 修饰符", ctx.previous)
			Modifier.FINAL in modifiers && Modifier.OVERRIDE in modifiers -> syntaxError("顶层函数不支持 'final override' 修饰符", ctx.previous)
		}
		return FunDeclarationParser(ctx).parse(modifiers)
	}
}

private object ClassDeclarationMatcher : TopLevelDeclarationMatcher<ClassDeclaration> {
	
	override val tokenType = FreeTokenType.CLASS
	
	override suspend fun parseAndCheck(ctx: FreeParserContext, modifiers: Set<Modifier>): ClassDeclaration {
		when {
			Modifier.OVERRIDE in modifiers -> syntaxError("类不支持 'override' 修饰符", ctx.previous)
			Modifier.FINAL in modifiers && Modifier.OVERRIDE in modifiers -> syntaxError("类不支持 'final override' 修饰符", ctx.previous)
		}
		return ClassDeclarationParser(ctx).parse(modifiers)
	}
}

private object SingleDeclarationMatcher : TopLevelDeclarationMatcher<SingleDeclaration> {
	
	override val tokenType = FreeTokenType.SINGLE
	
	override suspend fun parseAndCheck(ctx: FreeParserContext, modifiers: Set<Modifier>): SingleDeclaration {
		when {
			Modifier.OPEN in modifiers -> syntaxError("单例类不支持 'open' 修饰符", ctx.previous)
			Modifier.ABSTRACT in modifiers -> syntaxError("单例类不支持 'abstract' 修饰符", ctx.previous)
			Modifier.OVERRIDE in modifiers -> syntaxError("单例类不支持 'override' 修饰符", ctx.previous)
			Modifier.FINAL in modifiers && Modifier.OVERRIDE in modifiers -> syntaxError("单例类不支持 'final override' 修饰符", ctx.previous)
		}
		return SingleDeclarationParser(ctx).parse(modifiers)
	}
}

private object InterfaceDeclarationMatcher : TopLevelDeclarationMatcher<InterfaceDeclaration> {
	
	override val tokenType = FreeTokenType.INTERFACE
	
	override suspend fun parseAndCheck(ctx: FreeParserContext, modifiers: Set<Modifier>): InterfaceDeclaration {
		when {
			Modifier.OPEN in modifiers -> syntaxError("接口不支持 'open' 修饰符", ctx.previous)
			Modifier.ABSTRACT in modifiers -> syntaxError("接口不支持 'abstract' 修饰符", ctx.previous)
			Modifier.OVERRIDE in modifiers -> syntaxError("接口不支持 'override' 修饰符", ctx.previous)
			Modifier.FINAL in modifiers && Modifier.OVERRIDE in modifiers -> syntaxError("接口不支持 'final override' 修饰符", ctx.previous)
		}
		return InterfaceDeclarationParser(ctx).parse(modifiers)
	}
}

private object StructDeclarationMatcher : TopLevelDeclarationMatcher<StructDeclaration> {
	
	override val tokenType = FreeTokenType.STRUCT
	
	override suspend fun parseAndCheck(ctx: FreeParserContext, modifiers: Set<Modifier>): StructDeclaration {
		when {
			Modifier.OPEN in modifiers -> syntaxError("结构体不支持 'open' 修饰符", ctx.previous)
			Modifier.ABSTRACT in modifiers -> syntaxError("结构体不支持 'abstract' 修饰符", ctx.previous)
			Modifier.OVERRIDE in modifiers -> syntaxError("结构体不支持 'override' 修饰符", ctx.previous)
			Modifier.FINAL in modifiers && Modifier.OVERRIDE in modifiers -> syntaxError("结构体不支持 'final override' 修饰符", ctx.previous)
		}
		return StructDeclarationParser(ctx).parse(modifiers)
	}
}

private object EnumDeclarationMatcher : TopLevelDeclarationMatcher<EnumDeclaration> {
	
	override val tokenType = FreeTokenType.ENUM
	
	override suspend fun parseAndCheck(ctx: FreeParserContext, modifiers: Set<Modifier>): EnumDeclaration {
		when {
			Modifier.OPEN in modifiers -> syntaxError("枚举不支持 'open' 修饰符", ctx.previous)
			Modifier.ABSTRACT in modifiers -> syntaxError("枚举不支持 'abstract' 修饰符", ctx.previous)
			Modifier.OVERRIDE in modifiers -> syntaxError("枚举不支持 'override' 修饰符", ctx.previous)
			Modifier.FINAL in modifiers && Modifier.OVERRIDE in modifiers -> syntaxError("枚举不支持 'final override' 修饰符", ctx.previous)
		}
		return EnumDeclarationParser(ctx).parse(modifiers)
	}
}

private object AnnotationDeclarationMatcher : TopLevelDeclarationMatcher<AnnotationDeclaration> {
	
	override val tokenType = FreeTokenType.ANNOTATION
	
	override suspend fun parseAndCheck(ctx: FreeParserContext, modifiers: Set<Modifier>): AnnotationDeclaration {
		when {
			Modifier.OPEN in modifiers -> syntaxError("注解不支持 'open' 修饰符", ctx.previous)
			Modifier.ABSTRACT in modifiers -> syntaxError("注解不支持 'abstract' 修饰符", ctx.previous)
			Modifier.OVERRIDE in modifiers -> syntaxError("注解不支持 'override' 修饰符", ctx.previous)
			Modifier.FINAL in modifiers && Modifier.OVERRIDE in modifiers -> syntaxError("注解不支持 'final override' 修饰符", ctx.previous)
		}
		return AnnotationDeclarationParser(ctx).parse(modifiers)
	}
}