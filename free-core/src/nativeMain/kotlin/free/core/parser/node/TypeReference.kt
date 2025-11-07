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
		if (ctx.match(FreeTokenType.LPAREN)) {
			var lparenCount = 0
			while (ctx.match(FreeTokenType.LPAREN)) {
				lparenCount++
			}
			val parameters = mutableListOf<Parameter>()
			while (!ctx.match(FreeTokenType.RPAREN)) {
				parameters += ParameterParser(ctx).parseWhenLambda()
				if (!ctx.check(FreeTokenType.RPAREN)) {
					ctx.expect(FreeTokenType.COMMA, "函数参数列表缺少 ','")
				}
			}
			if (parameters.size == 1 && !ctx.check(FreeTokenType.ARROW)) {
				val parameter = parameters.single()
				if (parameter.name != "") {
					syntaxError("语法错误", ctx.peek(offset = -4)!!)
				}
				repeat(lparenCount) {
					ctx.expect(FreeTokenType.RPAREN, "函数参数缺少 ')'")
				}
				return parameter.typeReference
			}
			ctx.expect(FreeTokenType.ARROW, "函数参数缺少 '->'")
			val returnTypes = mutableListOf<TypeReference>()
			if (ctx.match(FreeTokenType.LBRACKET)) {
				while (!ctx.match(FreeTokenType.RBRACKET)) {
					returnTypes += parse()
					if (!ctx.check(FreeTokenType.RBRACKET)) {
						ctx.expect(FreeTokenType.COMMA, "函数参数返回类型缺少 ','")
					}
				}
				if (returnTypes.size < 2) {
					syntaxError("多返回类型必须至少2个", ctx.previous)
				}
			} else {
				returnTypes += parse()
			}
			repeat(lparenCount) {
				ctx.expect(FreeTokenType.RPAREN, "函数参数缺少 ')'")
			}
			val isArray = ctx.match(FreeTokenType.LBRACKET)
			if (isArray) {
				ctx.expect(FreeTokenType.RBRACKET, "数组类型缺少 ']'")
			}
			val isNullable = ctx.match(FreeTokenType.QUESTION)
			if (isNullable) {
				when {
					ctx.peek(offset = -2)?.type == FreeTokenType.IDENTIFIER -> {}
					ctx.peek(offset = -3)?.type == FreeTokenType.LBRACKET && ctx.peek(offset = 2)?.type == FreeTokenType.RBRACKET -> {}
					ctx.peek(offset = -2)?.type == FreeTokenType.RPAREN -> {}
					else -> syntaxError("语法错误", ctx.previous)
				}
			}
			return TypeReference(
				type = LambdaType(
					parameters = parameters,
					returnTypes = returnTypes,
				),
				isNullable = isNullable,
				isArray = isArray
			)
		} else {
		
		}
		
		// 普通类型
		ctx.expect(FreeTokenType.IDENTIFIER, "无法识别标识符")
		val type = mutableListOf<String>()
		type += ctx.previous.value
		while (ctx.match(FreeTokenType.DOT)) {
			ctx.expect(FreeTokenType.IDENTIFIER, "无法识别标识符")
			type += ctx.previous.value
		}
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