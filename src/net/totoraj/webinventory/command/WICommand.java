package net.totoraj.webinventory.command;

import java.util.ArrayList;
import java.util.List;

import net.totoraj.webinventory.WebInventory;
import net.totoraj.webinventory.config.WIMessages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class WICommand implements TabExecutor {

	private WebInventory plugin;

	public WICommand (WebInventory plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if (args.length == 0) {
			sender.sendMessage(WIMessages.getNotEnoughArguments());
			return true;
		}
		if (args[0].equals("signup")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(WIMessages.getNotPlayer());
//				sender.sendMessage("OK");
				return true;
			}
			Player player = (Player) sender;
			boolean _result = plugin.DATA_MANAGER.addPlayer(player.getUniqueId(), player.getName());
			if (_result) {
				sender.sendMessage(WIMessages.getSignUp());
			}
			else {
				sender.sendMessage(WIMessages.getFailedSignUp());
			}
		}
		return true;
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label,
			String[] args) {
		if (args.length == 1) {
			String prefix = args[0].toLowerCase();
			ArrayList<String> commands = new ArrayList<String>();
			if ("signup".startsWith(prefix)) {
				commands.add("signup");
			}
			return commands;
		}
		return null;
	}
}
