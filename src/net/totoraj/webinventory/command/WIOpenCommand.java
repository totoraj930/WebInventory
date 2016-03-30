package net.totoraj.webinventory.command;

import java.util.List;
import java.util.UUID;

import net.totoraj.webinventory.WebInventory;
import net.totoraj.webinventory.config.WIMessages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class WIOpenCommand implements TabExecutor{
	private WebInventory plugin;

	public WIOpenCommand (WebInventory plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if (args.length == 0) {
			sender.sendMessage(WIMessages.getNotEnoughArguments());
			return true;
		}
		if (args.length == 1) {
			Player targetPlayer = plugin.getServer().getPlayer(args[0]);
			if (targetPlayer == null) {
				sender.sendMessage(WIMessages.getNotExist());
				return true;
			}
			if (!plugin.DATA_MANAGER.isAvailable() || !plugin.CAN_USE) {
				targetPlayer.sendMessage(WIMessages.getCanNotUse());
				sender.sendMessage(WIMessages.getCanNotUse());
				return true;
			}
			if (targetPlayer.hasPermission("webinventory.use")) {
				UUID targetUUID = targetPlayer.getUniqueId();
				boolean result = plugin.INV_MANAGER.openInventory(targetUUID, targetUUID);
				if (!result) {
					targetPlayer.sendMessage(WIMessages.getCouldNotOpenInventory());
					sender.sendMessage(WIMessages.getCouldNotOpenInventory());
				}
			}
			else {
				targetPlayer.sendMessage(WIMessages.getDoNotHavePermission());
				sender.sendMessage(WIMessages.getDoNotHavePermission());
			}
		}
		return true;
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label,
			String[] args) {
		if(args.length == 1){
//			String prefix = args[0].toLowerCase();
//			ArrayList<String> commands = new ArrayList<String>();
//			return commands;
		}
		return null;
	}
}