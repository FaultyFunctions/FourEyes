package dev.faultyfunctions.fourEyes

import dev.faultyfunctions.fourEyes.tasks.ChiseledBookshelfTask
import org.bukkit.plugin.java.JavaPlugin

class FourEyes : JavaPlugin() {

	override fun onEnable() {
		// TASKS
		ChiseledBookshelfTask().runTaskTimer(this, 0, 2)

		logger.info("Enabled!")
	}

	override fun onDisable() {
		logger.info("Disabled!")
	}
}
