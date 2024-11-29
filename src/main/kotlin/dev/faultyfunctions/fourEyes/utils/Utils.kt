package dev.faultyfunctions.fourEyes.utils

import org.bukkit.block.Block
import org.bukkit.entity.Player


object Utils {
	fun getTargetBlock(player: Player): Block? {
		try {
			val lastBlock: Block? = player.getTargetBlockExact(5)
			return lastBlock
		} catch (_: IllegalStateException) {
		}
		return null
	}

	fun intToRoman(num: Int): String {
		val m_k = listOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
		val m_v = listOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")

		var str = ""
		var n = num

		for (i in m_k.indices) {
			while (n >= m_k[i]) {
				n -= m_k[i]
				str += m_v[i]
			}
		}
		return str
	}

	fun String.capitalizeWords(delimiter: String = " ") =
		split(delimiter).joinToString(delimiter) { word ->

			val smallCaseWord = word.lowercase()
			(if (word == "of") {
				smallCaseWord
			} else {
				smallCaseWord.replaceFirstChar(Char::titlecaseChar)
			}).toString()

		}
}