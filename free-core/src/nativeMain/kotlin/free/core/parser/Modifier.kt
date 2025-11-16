package free.core.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.Modifier.*

enum class Modifier {
	PRIVATE, FILE, INTERNAL, MODULE, PUBLIC,
	VAR, VAL,
	OPEN, ABSTRACT, OVERRIDE, FINAL, CONST,
	IGNORE
}

private val accessModifiers = setOf(PRIVATE, FILE, INTERNAL, MODULE, PUBLIC)

val Set<Modifier>.access: Modifier
	get() {
		return accessModifiers.find { it in this }
			?: error("未知的修饰符")
	}

val Set<Modifier>.isOpen: Boolean
	get() = OPEN in this

val Set<Modifier>.isAbstract: Boolean
	get() = ABSTRACT in this

context(_: FreeContext)
fun getTopLevelAccessModifier(ctx: FreeParserContext): Modifier {
	return when {
		ctx.match(FreeTokenType.PRIVATE) -> PRIVATE
		ctx.match(FreeTokenType.FILE) -> syntaxError("顶层声明不支持 'file' 修饰符", ctx.previous)
		ctx.match(FreeTokenType.INTERNAL) -> INTERNAL
		ctx.match(FreeTokenType.MODULE) -> MODULE
		ctx.match(FreeTokenType.PUBLIC) -> PUBLIC
		else -> PUBLIC
	}
}

context(_: FreeContext)
fun getMemberAccessModifier(ctx: FreeParserContext, parentAccess: Modifier, errorMessage: () -> String): Modifier {
	val memberAccess = when {
		ctx.match(FreeTokenType.PRIVATE) -> PRIVATE
		ctx.match(FreeTokenType.FILE) -> FILE
		ctx.match(FreeTokenType.INTERNAL) -> INTERNAL
		ctx.match(FreeTokenType.MODULE) -> MODULE
		ctx.match(FreeTokenType.PUBLIC) -> PUBLIC
		else -> null
	}
	return if (memberAccess != null) {
		if (!((parentAccess == PRIVATE && memberAccess <= FILE) || (memberAccess <= parentAccess))) {
			syntaxError(errorMessage(), ctx.previous)
		}
		memberAccess
	} else {
		getDefaultMemberAccessModifier(parentAccess)
	}
}

fun getDefaultMemberAccessModifier(parentAccess: Modifier): Modifier {
	return when (parentAccess) {
		PRIVATE, FILE -> FILE
		INTERNAL -> INTERNAL
		MODULE -> MODULE
		PUBLIC -> PUBLIC
		else -> error("不支持的访问修饰符")
	}
}

context(_: FreeContext)
fun getClassParameterAccessModifier(ctx: FreeParserContext, classAccess: Modifier, errorMessage: () -> String): Modifier? {
	val access = when {
		ctx.match(FreeTokenType.PRIVATE) -> PRIVATE
		ctx.match(FreeTokenType.FILE) -> FILE
		ctx.match(FreeTokenType.INTERNAL) -> INTERNAL
		ctx.match(FreeTokenType.MODULE) -> MODULE
		ctx.match(FreeTokenType.PUBLIC) -> PUBLIC
		else -> return null
	}
	if (!((classAccess == PRIVATE && access <= FILE) || (access <= classAccess))) {
		syntaxError(errorMessage(), ctx.previous)
	}
	return access
}

fun getDeclarationModifiers(ctx: FreeParserContext): Set<Modifier> {
	val modifiers = mutableSetOf<Modifier>()
	when {
		ctx.match(FreeTokenType.CONST) -> modifiers += CONST
		ctx.match(FreeTokenType.OPEN) -> modifiers += OPEN
		ctx.match(FreeTokenType.ABSTRACT) -> modifiers += ABSTRACT
		ctx.match(FreeTokenType.OVERRIDE) -> modifiers += OVERRIDE
		ctx.match(FreeTokenType.FINAL, FreeTokenType.OVERRIDE) -> {
			modifiers += FINAL
			modifiers += OVERRIDE
		}
	}
	return modifiers
}

context(_: FreeContext)
fun checkSupportedDeclarationModifiers(
	ctx: FreeParserContext,
	modifiers: Set<Modifier>,
	name: String,
	isSupportedConst: Boolean = false,
	isSupportedOpen: Boolean = false,
	isSupportedAbstract: Boolean = false,
	isSupportedFinalOverride: Boolean = false,
	isSupportedOverride: Boolean = false
) {
	when {
		CONST in modifiers && !isSupportedConst -> syntaxError("${name}不支持 'const' 修饰符", ctx.previous)
		OPEN in modifiers && !isSupportedOpen -> syntaxError("${name}不支持 'open' 修饰符", ctx.previous)
		ABSTRACT in modifiers && !isSupportedAbstract -> syntaxError("${name}不支持 'abstract' 修饰符", ctx.previous)
		FINAL in modifiers && OVERRIDE in modifiers && !isSupportedFinalOverride -> syntaxError("${name}不支持 \"final override\" 修饰符", ctx.previous)
		OVERRIDE in modifiers && !isSupportedOverride -> syntaxError("${name}不支持 'override' 修饰符", ctx.previous)
	}
}