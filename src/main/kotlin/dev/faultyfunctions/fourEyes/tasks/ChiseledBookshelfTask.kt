package dev.faultyfunctions.fourEyes.tasks

import dev.faultyfunctions.fourEyes.FourEyes
import dev.faultyfunctions.fourEyes.utils.Utils.capitalizeWords
import dev.faultyfunctions.fourEyes.utils.Utils.getTargetBlock
import dev.faultyfunctions.fourEyes.utils.Utils.intToRoman
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.ChiseledBookshelf
import org.bukkit.block.data.Directional
import org.bukkit.entity.Player
import org.bukkit.inventory.ChiseledBookshelfInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.RayTraceResult
import org.bukkit.util.Vector

class ChiseledBookshelfTask() : BukkitRunnable() {
	override fun run() {
		for (player: Player in Bukkit.getOnlinePlayers()) {
			val targetBlock: Block? = getTargetBlock(player)

			// ARE WE LOOKING AT A CHISELED BOOKSHELF?
			if (targetBlock != null && targetBlock.blockData.material == Material.CHISELED_BOOKSHELF) {
				val shelf: ChiseledBookshelf = targetBlock.state as ChiseledBookshelf
				val lookRayTrace: RayTraceResult = player.rayTraceBlocks(6.0) ?: return

				// CHECK TO MAKE SURE WE ARE LOOKING AT THE CORRECT BLOCK FACE ON THE BOOKSHELF
				val rayTraceBlockFace: BlockFace = lookRayTrace.hitBlockFace ?: return
				val blockDirectional: Directional = shelf.blockData as Directional
				if (blockDirectional.facing != rayTraceBlockFace) {
					FourEyes.plugin.adventure().player(player).sendActionBar(Component.text(""))
					return
				}

				// GET VECTOR FROM SHELF'S POSITION TO WHERE WE ARE LOOKING ON THE BLOCK AND USE THAT TO GET THE ITEM IN
				// THE PROPER SLOT
				val slotVector: Vector = lookRayTrace.hitPosition.subtract(shelf.location.toVector())
				val slot = shelf.getSlot(slotVector)
				val shelfInventory: ChiseledBookshelfInventory = shelf.inventory
				val bookInSlot: ItemStack? = shelfInventory.getItem(slot) // RETURN IF THERE IS NO BOOK
				if (bookInSlot == null) {
					FourEyes.plugin.adventure().player(player).sendActionBar(Component.text(""))
					return
				}
				val bookInSlotMeta: ItemMeta? = bookInSlot.itemMeta

				// SEND PLAYER THE TITLE OF THE BOOK IF IT HAS A TITLE
				if (bookInSlotMeta is BookMeta && bookInSlotMeta.hasTitle() && bookInSlotMeta.hasAuthor()) {
					FourEyes.plugin.adventure().player(player).sendActionBar(Component.text(bookInSlotMeta.title.toString() + " by " + bookInSlotMeta.author.toString()))
				} else if (bookInSlotMeta is EnchantmentStorageMeta) {
					var bookTitle: TextComponent.Builder = Component.text().append(Component.text("Enchanted Book", NamedTextColor.YELLOW)).append(Component.text(" ("))
					var i = 0
					for ((enchantment, level) in bookInSlotMeta.storedEnchants) {
						val namespaceString: String = enchantment.key.namespace + ":"
						bookTitle = bookTitle.append(Component.text(enchantment.key.toString().replace(namespaceString, "").replace("_", " ").capitalizeWords()))
						bookTitle = bookTitle.append(Component.text(" " + intToRoman(level)))
						if (bookInSlotMeta.storedEnchants.size > 1 && i != (bookInSlotMeta.storedEnchants.size - 1)) {
							bookTitle = bookTitle.append(Component.text(", "))
						}
						++i
					}
					bookTitle = bookTitle.append(Component.text(")"))
					FourEyes.plugin.adventure().player(player).sendActionBar(bookTitle.build())
				} else if (bookInSlotMeta != null) {
					if (bookInSlotMeta.hasDisplayName()) {
						FourEyes.plugin.adventure().player(player).sendActionBar(Component.text(bookInSlotMeta.displayName))
					} else {
						FourEyes.plugin.adventure().player(player).sendActionBar(Component.text(""))
					}
				}
			}
		}
	}
}