package net.totoraj.webinventory.command;

import java.util.ArrayList;
import java.util.List;

import net.totoraj.webinventory.Utility;
import net.totoraj.webinventory.WebInventory;
import net.totoraj.webinventory.config.WIMessages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class WIAdminCommand implements TabExecutor {

	private WebInventory plugin;

	public WIAdminCommand (WebInventory plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if (args.length == 0) {
			sender.sendMessage(WIMessages.getNotEnoughArguments());
			return true;
		}
		if (args[0].equals("toggle")) {
			plugin.CAN_USE = !plugin.CAN_USE;
			if (plugin.CAN_USE) {
				sender.sendMessage(WIMessages.getInventoryEnabled());
			}
			else {
				// 無効にしたらインベントリをすべて閉じる
				plugin.INV_MANAGER.closeAllInventory();
				sender.sendMessage(WIMessages.getInventoryDisabled());
			}
			return true;
		}
		if (args[0].equals("close")) {
			plugin.INV_MANAGER.closeAllInventory();
			sender.sendMessage(WIMessages.getCloseAllInventory());
			return true;
		}

		if (args[0].equals("reload")) {
			WIMessages.reload();
			sender.sendMessage(WIMessages.getReloadMessagesYaml());
			return true;
		}

		if (args[0].equals("yaml")) {
			sender.sendMessage(Utility.replaceColorCode("&4未実装です❤"));
			return true;
		}
		return true;
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label,
			String[] args) {
		if (args.length == 1) {
			String prefix = args[0].toLowerCase();
			ArrayList<String> commands = new ArrayList<String>();
			if ("reload".startsWith(prefix)) {
				commands.add("reload");
			}
			if ("yaml".startsWith(prefix)) {
				commands.add("yaml");
			}
			if ("toggle".startsWith(prefix)) {
				commands.add("toggle");
			}
			if ("close".startsWith(prefix)) {
				commands.add("close");
			}
			return commands;
		}
		return null;
	}
}
