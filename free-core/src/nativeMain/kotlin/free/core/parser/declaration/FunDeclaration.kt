package free.core.parser.declaration

import free.core.constants.FreeTypes
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.node.FunParameterParser
import free.core.parser.node.Parameter
import free.core.parser.node.TypeReference
import free.core.parser.node.TypeReferenceParser
import kotlinx.serialization.Serializable

@Serializable
data class FunDeclaration(
	val name: String,
	val parameters: List<Parameter>,
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
		
		val parameters = mutableListOf<Parameter>()
		while (!ctx.match(FreeTokenType.RPAREN)) {
			parameters += FunParameterParser(ctx).parse()
			if (!ctx.check(FreeTokenType.RPAREN)) {
				ctx.expect(FreeTokenType.COMMA, "函数 $funName 的参数缺少 ','")
			}
		}
		
		val returnTypes = mutableListOf<TypeReference>()
		if (ctx.match(FreeTokenType.COLON)) {
			do {
				returnTypes += TypeReferenceParser(ctx).parse()
			} while (ctx.match(FreeTokenType.COMMA))
		} else {
			returnTypes += TypeReference(FreeTypes.Unit)
		}
		
		ctx.expect(FreeTokenType.LBRACE, "函数 $funName 缺少 '{'")
		
		while (!ctx.match(FreeTokenType.RBRACE)) {
			ctx.advance()
		}
		return FunDeclaration(
			name = funName,
			parameters = parameters,
			modifiers = modifiers.toList(),
			returnTypes = returnTypes,
//			statements = emptyList(),
		)
	}
}