package net.totoraj.webinventory;

import java.util.HashMap;
import java.util.List;

import net.totoraj.webinventory.command.WIAdminCommand;
import net.totoraj.webinventory.command.WICommand;
import net.totoraj.webinventory.command.WIOpenCommand;
import net.totoraj.webinventory.command.WIViewCommand;
import net.totoraj.webinventory.config.WIConfig;
import net.totoraj.webinventory.config.WIMessages;
import net.totoraj.webinventory.listener.InventoryListener;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class WebInventory extends JavaPlugin {
	private HashMap<String, TabExecutor> COMMANDS;
	public WIConfig CONFIG;
	public DataManager DATA_MANAGER;
	public InventoryManager INV_MANAGER;

	public boolean CAN_USE = true;

	public void info(String msg) {
		this.getLogger().info(Utility.replaceColorCode(msg));
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, msg);
	}

	@Override
	public void onEnable() {
		// Configを読み込み
		CONFIG = new WIConfig(this, "config.yml");
		CONFIG.saveDefaultYaml();
		CONFIG.loadYaml();

		// メッセージを初期化
		WIMessages.initialize(this, "messages.yml");

		// DataManagerを初期化
		DATA_MANAGER = new DataManager(this);
		// InventoryManagerを初期化
		INV_MANAGER = new InventoryManager(this);

		// コマンド登録
		COMMANDS = new HashMap<String, TabExecutor>();
		COMMANDS.put("webinventoryadmin", new WIAdminCommand(this));
		COMMANDS.put("webinventoryopen", new WIOpenCommand(this));
		COMMANDS.put("webinventoryview", new WIViewCommand(this));
		COMMANDS.put("webinventory", new WICommand(this));

		// イベントリスナ登録
		getServer().getPluginManager().registerEvents(
				new InventoryListener(this), this);

		// データベースセットアップ
		DATA_MANAGER.setUp();

		info(WIMessages.getPluginEnabled());

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		return COMMANDS.get(command.getName()).onCommand(sender, command,
				label, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String label, String[] args) {
		return COMMANDS.get(command.getName()).onTabComplete(sender, command,
				label, args);
	}

	@Override
	public void onDisable() {
		if (DATA_MANAGER.getMySQL().close()) {
			getLogger().info("データベースから切断しました");
		}

	}
}
