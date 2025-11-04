@file:OptIn(ExperimentalForeignApi::class)

package free.core.io

import kotlinx.cinterop.*
import platform.posix.*

class File(
	private val path: String
) {
	
	private var _absolutePath: String? = null
	val absolutePath: String
		get() {
			if (_absolutePath != null) return _absolutePath!!
			return memScoped {
				val resolved = realpath(path, null)
				if (resolved != null) {
					val absPath = resolved.toKString()
					free(resolved)
					return absPath
				}
				val combined = if (path.startsWith("/")) {
					path
				} else {
					val cwd = currentWorkingDirectory()
					"$cwd/$path"
				}
				val normalized = realpath(combined, null)
				if (normalized != null) {
					val abs = normalized.toKString()
					free(normalized)
					return abs
				}
				combined
			}.also { _absolutePath = it }
		}
	
	fun readFileChars(): CharArray {
		val file = fopen(path, "r") ?: error("Cannot open file: $path")
		try {
			val buffers = StringBuilder()
			memScoped {
				val buf = allocArray<ByteVar>(4096)
				while (true) {
					val bytesRead = fread(buf, 1u, 4096u, file)
					if (bytesRead == 0UL) break
					buffers.append(buf.toKString())
				}
			}
			return buffers.toString().toCharArray()
		} finally {
			fclose(file)
		}
	}
}

fun currentWorkingDirectory(): String {
	val cwdPtr = getcwd(null, 0u)
	val cwd = cwdPtr?.toKString() ?: "."
	if (cwdPtr != null) free(cwdPtr)
	return cwd
}