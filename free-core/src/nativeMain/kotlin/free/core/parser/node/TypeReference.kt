package free.core.parser.node

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import kotlinx.serialization.Serializable

@Serializable
data class TypeReference(
	val type: Type,
	val isNullable: Boolean = false,
	val isArray: Boolean = false
)

@Serializable
sealed interface Type

@Serializable
data class NamedType(
	val value: String
) : Type

@Serializable
data class LambdaType(
	val parameters: List<Parameter>,
	val returnTypes: List<TypeReference>
) : Type

class TypeReferenceParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): TypeReference {
		return if (ctx.match(FreeTokenType.LPAREN)) {
			parseLambda()
		} else {
			parseNamed()
		}
	}
	
	private suspend fun parseLambda(): TypeReference {
		val parameters = mutableListOf<Parameter>()
		while (!ctx.match(FreeTokenType.RPAREN)) {
			parameters += LambdaParameterParser(ctx).parse()
			if (!ctx.check(FreeTokenType.RPAREN)) {
				ctx.expect(FreeTokenType.COMMA, "函数参数缺少 ','")
			}
		}
		if (ctx.match(FreeTokenType.ARROW)) {
			val returnTypes = mutableListOf<TypeReference>()
			if (ctx.match(FreeTokenType.LBRACKET)) {    // 多返回类型
				while (!ctx.match(FreeTokenType.RBRACKET)) {
					returnTypes += this.parse()
					if (!ctx.check(FreeTokenType.RBRACKET)) {
						ctx.expect(FreeTokenType.COMMA, "函数参数缺少 ','")
					}
				}
				if (returnTypes.isEmpty()) {
					syntaxError("函数参数多返回值类型语法错误", ctx.previous)
				}
				if (returnTypes.size == 1) {
					syntaxError("函数参数多返回值类型至少需要2个", ctx.previous)
				}
			} else {
				returnTypes += this.parse()
			}
			val isArray = ctx.match(FreeTokenType.LBRACKET)
			if (isArray) {
				val type = ctx.peek(offset = -2)?.type
				if (type != FreeTokenType.IDENTIFIER && type != FreeTokenType.RPAREN) {
					syntaxError("语法错误", ctx.previous)
				}
				ctx.expect(FreeTokenType.RBRACKET, "数组类型缺少 ']'")
			}
			val isNullable = ctx.match(FreeTokenType.QUESTION)
			if (isNullable) {
				val type = ctx.peek(offset = -2)?.type
				if (
					type != FreeTokenType.RPAREN &&
					type != FreeTokenType.IDENTIFIER &&
					(type != FreeTokenType.RBRACKET || ctx.peek(offset = -3)?.type != FreeTokenType.LBRACKET)
				) {
					syntaxError("语法错误", ctx.previous)
				}
			}
			return TypeReference(
				type = LambdaType(
					parameters = parameters,
					returnTypes = returnTypes
				),
				isNullable = isNullable,
				isArray = isArray
			)
		} else {
			if (parameters.size != 1) {
				syntaxError("函数参数缺少 '->'", ctx.current)
			}
			val parameter = parameters.single()
			if (parameter.name != "") {
				syntaxError("语法错误", ctx.peek(offset = -4)!!)
			}
			val isArray = ctx.match(FreeTokenType.LBRACKET)
			if (isArray) {
				ctx.expect(FreeTokenType.RBRACKET, "数组类型缺少 ']'")
			}
			val isNullable = ctx.match(FreeTokenType.QUESTION)
			val type = parameter.typeReference.type
			return TypeReference(
				type = type,
				isNullable = isNullable,
				isArray = isArray
			)
		}
	}
	
	private suspend fun parseNamed(): TypeReference {
		val type = mutableListOf<String>()
		do {
			ctx.expect(FreeTokenType.IDENTIFIER, "无法识别标识符")
			type += ctx.previous.value
		} while (ctx.match(FreeTokenType.DOT))
		val isArray = ctx.match(FreeTokenType.LBRACKET)
		if (isArray) {
			ctx.expect(FreeTokenType.RBRACKET, "数组类型缺少 ']'")
		}
		val isNullable = ctx.match(FreeTokenType.QUESTION)
		return TypeReference(
			type = NamedType(type.joinToString(".")),
			isNullable = isNullable,
			isArray = isArray
		)
	}
}