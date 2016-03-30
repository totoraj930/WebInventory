package net.totoraj.webinventory.command;

import java.util.List;
import java.util.UUID;

import net.totoraj.webinventory.WebInventory;
import net.totoraj.webinventory.config.WIMessages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class WIViewCommand implements TabExecutor{
	private WebInventory plugin;

	public WIViewCommand (WebInventory plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if (!plugin.DATA_MANAGER.isAvailable()) {
			sender.sendMessage(WIMessages.getCanNotUse());
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(WIMessages.getNotEnoughArguments());
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(WIMessages.getNotPlayer());
			return true;
		}
		if (args.length == 1) {
			if (!plugin.CAN_USE) {
				sender.sendMessage(WIMessages.getCanNotUse());
				return true;
			}
			Player viewer = (Player) sender;
			Player targetPlayer = plugin.getServer().getPlayer(args[0]);
			if (targetPlayer == null) {
				sender.sendMessage(WIMessages.getNotExist());
				return true;
			}
			UUID viewerUUID = viewer.getUniqueId();
			UUID targetUUID = targetPlayer.getUniqueId();
			boolean result = plugin.INV_MANAGER.openInventory(viewerUUID, targetUUID);
			if (!result) {
				sender.sendMessage(WIMessages.getCouldNotOpenInventory());
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
