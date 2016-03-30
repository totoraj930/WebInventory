package net.totoraj.webinventory;

import java.util.UUID;

import net.totoraj.webinventory.config.WIMessages;
import net.totoraj.webinventory.sql.IdTable;
import net.totoraj.webinventory.sql.ItemTable;
import net.totoraj.webinventory.sql.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DataManager {
	private WebInventory plugin;
	private YamlConfiguration yaml;
	private final String Address, Username, Password, Db, Prefix;
	private final int Port;
	private final MySQL mysql;
	private final IdTable idtable;
	private final ItemTable itemtable;
	private YamlConfiguration temp_yaml;
	private boolean available;

	public DataManager(WebInventory plugin) {
		this.plugin = plugin;

		// configからいろいろ取得
		yaml = plugin.CONFIG.getYaml();
		Address = yaml.getString("Database.Address", "localhost");
		Port = yaml.getInt("Database.Port", 3306);
		Username = yaml.getString("Database.Username", "Username");
		Password = yaml.getString("Database.Password", "Password");
		Db = yaml.getString("Database.Db", "Db");
		Prefix = yaml.getString("Database.Prefix", "wi_");

		// 変数の初期化
		temp_yaml = new YamlConfiguration();

		// MySQLのインスタンスを作成
		mysql = new MySQL(Address, Port, Username, Password, Db);

		// Tableのインスタンスを作成
		idtable = new IdTable(this);
		itemtable = new ItemTable(this);

	}

	public void setUp() {
		// MySQLに接続
		available = mysql.connect();

		// 接続されていれば初期処理
		if (available) {
			plugin.info(WIMessages.getConnectedDb());
			available = idtable.setUpTable() && itemtable.setUpTable();
		}
		// 接続されていなければ終了
		else {
			plugin.info(WIMessages.getFailedConncectDb());
		}

		if (available) {
			plugin.info(WIMessages.getTableIsReady());
		} else {
			plugin.info(WIMessages.getFailedReadyTable());
		}

	}

	// データが利用可能かを返す
	public boolean isAvailable() {
		return available;
	}

	// MySQLを返す
	public MySQL getMySQL() {
		return mysql;
	}

	// Prefixを返す
	public String getPrefix() {
		return Prefix;
	}

	// IdTableを返す
	public IdTable getIdTable () {
		return idtable;
	}

	// ItemTabelを返す
	public ItemTable getItemTable () {
		return itemtable;
	}


	// データベースのアイテムレコードからインベントリを生成する
	public Inventory getInventory(UUID uuid) {
		int player_id = idtable.getPlayerIdFromUUID(uuid);

		// 登録されていなければnull
		if (player_id == 0) {
			return null;
		}
		String item_stacks[] = itemtable.getItemRecords(player_id);
		String playerName = plugin.getServer().getOfflinePlayer(uuid).getName();

		Inventory inventory = Bukkit.createInventory(null, 9*6, WIMessages.getInventoryTitleText().replaceAll("<PLAYER>", playerName));
		int count = 0;
		for (String item_stack : item_stacks) {
			if (item_stack == null) {
				count++;
				continue;
			}
			try {
				temp_yaml.loadFromString(item_stack);
				inventory.setItem(count, temp_yaml.getItemStack("item"));
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
			count++;
		}

//		HashMap<Integer, String> item_map = itemtable.getItems2(player_id);
//		for (int key : item_map.keySet()) {
//			try {
//				temp_yaml.loadFromString(item_map.get(key));
//				inventory.setItem(key, temp_yaml.getItemStack("item"));
//			} catch (InvalidConfigurationException e) {
//				e.printStackTrace();
//			}
//		}
		return inventory;
	}


	// データベースにアイテムレコードの更新リクエストを投げる
	public boolean setInventory(UUID uuid, Inventory inv) {
		if (inv == null || inv.getSize() != 54) {
			return false;
		}

		// データベースにプレイヤーが存在するか
		int player_id = idtable.getPlayerIdFromUUID(uuid);
		if (player_id == 0) {
			return false;
		}


		String item_stacks[] = new String[54];
		int count = 0;
		for (ItemStack item : inv.getContents()) {
			if (item == null) {
				item_stacks[count] = null;
				count++;
				continue;
			}
			temp_yaml.set("item", item);
			item_stacks[count] = temp_yaml.saveToString();
			count++;
		}

		return itemtable.updateItemRecords(player_id, item_stacks);


//		HashMap<Integer, String> item_map = new HashMap<Integer, String>();
//		for (ItemStack item : inv.getContents()){
//			if (item != null) {
//				temp_yaml.set("item", item);
//				item_map.put(count, temp_yaml.saveToString());
//			}
//			count++;
//		}
//		return itemtable.setItems(player_id, item_map);
	}


	// プレイヤーを追加する
	public boolean addPlayer(UUID uuid, String playerName) {
		if(!idtable.addPlayer(uuid, playerName)) {
			return false;
		}

		return true;
	}


}
