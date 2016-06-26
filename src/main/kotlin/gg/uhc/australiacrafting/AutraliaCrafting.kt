package gg.uhc.australiacrafting

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import kotlin.properties.Delegates

const val PERMISSION = "uhc.flipcrafts"

class Entry : JavaPlugin() {
    var flipped: Boolean by Delegates.observable(false) { prop, old, new -> if (old != new) {
        flip()
        config.set("flipped", new)
        saveConfig()
    }}

    fun flip() = server.recipeIterator()
        .removeInstances(ShapedRecipe::class.java)
        .forEach {
            it.flip()
            server.addRecipe(it)
        }

    override fun onEnable() {
        config.options().copyDefaults(true)
        saveConfig()
        flipped = config.getBoolean("flipped")

        if (!config.getBoolean("skip update check")) {
            server.scheduler.runTaskTimerAsynchronously(this, UpdateManager(this), 0, 60 * 60 * 20)
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage("${ChatColor.RED}You do not have permission to use this command")
            return true
        }

        flipped = !flipped
        server.broadcastMessage("${ChatColor.GOLD}Recipies have been ${if (flipped) "flipped" else "returned to normal"}")
        return true
    }
}

internal inline fun <T, reified B> MutableIterator<T>.removeInstances(clazz: Class<B>) : List<B> {
    val removals = mutableListOf<B>()
    while (this.hasNext()) {
        val next = this.next()

        if (next is B) {
            this.remove()
            removals.add(next)
        }
    }
    return removals
}

fun ShapedRecipe.flip() = this.shape(* this.shape.reversedArray())