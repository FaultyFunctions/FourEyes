package dev.faultyfunctions.fourEyes

import dev.faultyfunctions.fourEyes.tasks.ChiseledBookshelfTask
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.java.JavaPlugin
import javax.annotation.Nonnull

class FourEyes : JavaPlugin() {
	companion object {
		lateinit var plugin: FourEyes
	}

	private lateinit var adventure: BukkitAudiences
	@Nonnull
	fun adventure(): BukkitAudiences {
		return this.adventure
	}

	override fun onEnable() {
		plugin = this
		plugin.adventure = BukkitAudiences.create(plugin)
		// TASKS
		ChiseledBookshelfTask().runTaskTimer(this, 0, 2)

		logger.info("Enabled!")
	}

	override fun onDisable() {
		logger.info("Disabled!")
	}
}
