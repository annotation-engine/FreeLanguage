package free.core.parser.parameter

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.expression.Expression
import free.core.parser.expression.matcher.parseCompleteExpression
import free.core.parser.node.TypeReference
import free.core.parser.node.TypeReferenceParser
import kotlinx.serialization.Serializable

@Serializable
data class Parameter(
	val name: String?,
	val modifiers: Set<Modifier>,
	val typeReference: TypeReference,
	val defaultExpression: Expression? = null
)

context(_: FreeContext)
fun parseParameter(
	ctx: FreeParserContext,
	modifiers: Set<Modifier> = emptySet(),
	isSupportedAnonymous: Boolean = false,
	isSupportedLambdaType: Boolean = true
): Parameter {
	val name = if (isSupportedAnonymous && ctx.peek(offset = 1)?.type != FreeTokenType.COLON) null else {
		ctx.expect(FreeTokenType.IDENTIFIER, "参数缺少名称")
		ctx.previous.value.also {
			ctx.expect(FreeTokenType.COLON, "参数缺少 ':'")
		}
	}
	val typeReference = TypeReferenceParser(ctx).parse(
		isSupportedLambdaType = isSupportedLambdaType
	)
	val defaultExpression = if (ctx.match(FreeTokenType.ASSIGN)) {
		parseCompleteExpression(ctx)
	} else null
	return Parameter(
		name = name,
		modifiers = modifiers,
		typeReference = typeReference,
		defaultExpression = defaultExpression
	)
}