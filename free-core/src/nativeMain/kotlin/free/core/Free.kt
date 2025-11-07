package free.core

import free.core.io.File
import free.core.lexer.FreeLexer
import free.core.parser.FreeParser
import free.core.parser.node.Program
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.time.measureTime

fun main(vararg args: String) {
	val duration = measureTime {
		val command = args.firstOrNull() ?: run {
			help()
			return
		}
		when (command) {
			"run" -> {
				val paths = args.drop(1)
				if (paths.isEmpty()) {
					println("Error: Missing file path.\nUsage: free run <file1.free> [file2.free ...]")
					return
				}
				run(args.drop(1))
			}
			
			else -> help()
		}
	}
	println("Execution time: $duration")
}

private fun run(paths: List<String>) = runBlocking {
	val files = paths.toSet().map(::File).distinctBy { it.absolutePath }
	val jobs = files.map { file ->
		val sourcePath = file.absolutePath
		val freeContext = FreeContext(sourcePath)
		async(Dispatchers.Default + freeContext) {
			val input = file.readFileChars()
			val rawTokens = FreeLexer(input).lex()
			FreeParser(rawTokens).parse()
		}
	}
	val sourceFileNodes = jobs.awaitAll()
	val program = Program(sourceFileNodes)
	println(json.encodeToString(program))
}

val json = Json {
	prettyPrint = true
	encodeDefaults = true
}

private fun help() {
	val help = """
		Usage:
            free run <file1.free> [file2.free ...]   Run one or more Free source files.
            free help                                Show command usage and options.
	""".trimIndent()
	println(help)
}

class FreeContext(
	val sourcePath: String
) : AbstractCoroutineContextElement(Key) {
	companion object Key : CoroutineContext.Key<FreeContext>
}