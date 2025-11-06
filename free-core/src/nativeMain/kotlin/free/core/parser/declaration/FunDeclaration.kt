package free.core.parser.declaration

import free.core.constrants.FreeTypes
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.node.TypeReference
import free.core.parser.node.TypeReferenceParser
import kotlinx.serialization.Serializable

@Serializable
data class FunDeclaration(
	val name: String,
//	val parameters: List<Parameter>,
	val modifiers: List<Modifier>,
	val returnTypes: List<TypeReference>,
//	val statements: List<Statement>
) : Declaration

class FunDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(vararg modifiers: Modifier): FunDeclaration {
		ctx.expect(FreeTokenType.IDENTIFIER, "函数缺少名称")
		val funName = ctx.previous.value
		ctx.expect(FreeTokenType.LPAREN, "函数 $funName 缺少 '('")
		
		while (!ctx.match(FreeTokenType.RPAREN)) {
			ctx.advance()
		}
		
		val returnTypes = mutableListOf<TypeReference>()
		if (ctx.match(FreeTokenType.COLON)) {
			if (ctx.match(FreeTokenType.LPAREN)) {
				while (!ctx.match(FreeTokenType.RPAREN)) {
					returnTypes += TypeReferenceParser(ctx).parse()
					if (!ctx.match(FreeTokenType.COMMA)) {
						if (!ctx.match(FreeTokenType.RPAREN)) {
							ctx.expect(FreeTokenType.LBRACE, "函数 $funName 函数缺少多返回类型时闭合符 ')'")
						}
						break
					}
				}
				if (returnTypes.size <= 1) {
					syntaxError("函数 $funName 使用多返回类型时，至少需要2个类型", ctx.current)
				}
			} else {
				returnTypes += TypeReferenceParser(ctx).parse()
			}
		} else {
			returnTypes += TypeReference(FreeTypes.UNIT)
		}
		
		ctx.expect(FreeTokenType.LBRACE, "函数 $funName 函数 '{'")
		
		while (!ctx.match(FreeTokenType.RBRACE)) {
			ctx.advance()
		}
		return FunDeclaration(
			name = funName,
//			parameters = emptyList(),
			modifiers = modifiers.toList(),
			returnTypes = returnTypes,
//			statements = emptyList(),
		)
	}
}