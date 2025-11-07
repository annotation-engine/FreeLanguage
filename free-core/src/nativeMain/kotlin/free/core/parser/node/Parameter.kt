package free.core.parser.node

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.checkMemberAccess
import free.core.parser.getDefaultMemberAccess
import kotlinx.serialization.Serializable

@Serializable
data class Parameter(
	val name: String,
	val modifiers: Set<Modifier>,
	val typeReference: TypeReference,
)

class LambdaParameterParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): Parameter {
		val name = if (ctx.peek(offset = 1)?.type == FreeTokenType.COLON) {
			ctx.expect(FreeTokenType.IDENTIFIER, "参数缺少名称")
			ctx.previous.value.also {
				ctx.expect(FreeTokenType.COLON, "参数缺少 ':'")
			}
		} else ""
		val typeReference = TypeReferenceParser(ctx).parse()
		return Parameter(
			name = name,
			modifiers = emptySet(),
			typeReference = typeReference,
		)
	}
}

class FunParameterParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += if (ctx.match(FreeTokenType.VAR)) Modifier.VAR else Modifier.VAL
		ctx.expect(FreeTokenType.IDENTIFIER, "参数缺少名称")
		val name = ctx.previous.value
		ctx.expect(FreeTokenType.COLON, "参数缺少 ':'")
		val typeReference = TypeReferenceParser(ctx).parse()
		return Parameter(
			name = name,
			modifiers = modifiers,
			typeReference = typeReference
		)
	}
}

class ClassParameterParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(classAccess: Modifier): Parameter {
		return when {
			ctx.match(FreeTokenType.PRIVATE) -> parse(classAccess, Modifier.PRIVATE)
			ctx.match(FreeTokenType.FILE) -> parse(classAccess, Modifier.FILE)
			ctx.match(FreeTokenType.INTERNAL) -> parse(classAccess, Modifier.INTERNAL)
			ctx.match(FreeTokenType.MODULE) -> parse(classAccess, Modifier.MODULE)
			ctx.match(FreeTokenType.PUBLIC) -> parse(classAccess, Modifier.PUBLIC)
			else -> parse(classAccess, null)
		}
	}
	
	/**
	 * private var name: String
	 * var name: String 自动推导访问级别
	 * name: String
	 */
	private suspend fun parse(classAccess: Modifier, parameterAccess: Modifier?): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		if (parameterAccess != null) {
			checkMemberAccess(classAccess, parameterAccess, ctx.previous)
			modifiers += parameterAccess
			modifiers += when {
				ctx.match(FreeTokenType.VAR) -> Modifier.VAR
				ctx.match(FreeTokenType.VAL) -> Modifier.VAL
				else -> syntaxError("主构造参数使用访问修饰符后必须跟 var 或 val", ctx.current)
			}
		} else {
			when {
				ctx.match(FreeTokenType.VAR) -> modifiers += Modifier.VAR
				ctx.match(FreeTokenType.VAL) -> modifiers += Modifier.VAL
			}
			if (modifiers.isNotEmpty()) {
				modifiers += getDefaultMemberAccess(classAccess, ctx.previous)
			}
		}
		ctx.expect(FreeTokenType.IDENTIFIER, "主构造参数缺少名称")
		val name = ctx.previous.value
		ctx.expect(FreeTokenType.COLON, "主构造参数缺少 ':'")
		val typeReference = TypeReferenceParser(ctx).parse()
		return Parameter(
			name = name,
			modifiers = modifiers,
			typeReference = typeReference
		)
	}
}