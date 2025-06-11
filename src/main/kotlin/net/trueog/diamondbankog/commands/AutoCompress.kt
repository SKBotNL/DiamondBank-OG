package net.trueog.diamondbankog.commands

import net.trueog.diamondbankog.Config
import net.trueog.diamondbankog.DiamondBankOG
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class AutoCompress : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        if (DiamondBankOG.economyDisabled) {
            sender.sendMessage(DiamondBankOG.mm.deserialize("${Config.prefix}<reset>: <red>The economy is disabled. Please notify a staff member."))
            return true
        }

        if (sender !is Player) {
            sender.sendMessage("You can only execute this command as a player.")
            return true
        }

        if (!sender.hasPermission("diamondbank-og.compress")) {
            sender.sendMessage(DiamondBankOG.mm.deserialize("${Config.prefix}<reset>: <red>You do not have permission to use this command."))
            return true
        }

        if (DiamondBankOG.redis.getValue("diamondbankog:${sender.uniqueId}:autocompress") == "true") {
            DiamondBankOG.redis.setValue("diamondbankog:${sender.uniqueId}:autocompress", "false")
            sender.sendMessage(DiamondBankOG.mm.deserialize("${Config.prefix}<reset>: <green>Successfully turned auto-compress <red>off<green>."))
        } else {
            if (DiamondBankOG.redis.getValue("diamondbankog:${sender.uniqueId}:autodeposit") == "true") {
                sender.sendMessage(DiamondBankOG.mm.deserialize("${Config.prefix}<reset>: <#FFA500>You cannot have both auto-compress and auto-deposit on at the same time, so auto-deposit has been turned <red>off<#FFA500>."))
                DiamondBankOG.redis.setValue("diamondbankog:${sender.uniqueId}:autodeposit", "false")
            }
            DiamondBankOG.redis.setValue("diamondbankog:${sender.uniqueId}:autocompress", "true")
            sender.sendMessage(DiamondBankOG.mm.deserialize("${Config.prefix}<reset>: <green>Successfully turned auto-compress on."))
        }
        return true
    }

}