package free.core

import free.core.io.File
import free.core.lexer.lexer
import kotlin.time.measureTime

fun main(vararg args: String) {
	val duration = measureTime {
		val command = args.firstOrNull() ?: run {
			help()
			return
		}
		when (command) {
			"run" -> run(args.drop(1))
			else -> help()
		}
	}
	println("Execution time: $duration")
}

private fun run(paths: List<String>) {
	if (paths.isEmpty()) {
		println("Error: Missing file path.\nUsage: free run <file1.free> [file2.free ...]")
		return
	}
	paths.forEach { path ->
		val file = File(path)
		val input = file.readFileChars()
		println("lexer -> ${file.absolutePath}")
		val tokens = lexer(input)
		println(tokens.size)
	}
}

private fun help() {
	val help = """
		Usage:
            free run <file1.free> [file2.free ...]   Run one or more Free source files.
            free help                                Show command usage and options.
	""".trimIndent()
	println(help)
}