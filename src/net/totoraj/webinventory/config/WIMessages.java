package net.totoraj.webinventory.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.annotation.Nonnull;

import net.totoraj.webinventory.Utility;
import net.totoraj.webinventory.WebInventory;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class WIMessages {

	private static WebInventory plugin;
	private static WIMessages instance;
	private static String fileName;
	private static File configFile;
	private static YamlConfiguration resources;


	// info_messages
	private String pluginEnabled;
	private String pluginDisabled;
	private String connectedDb;
	private String disconnectedDb;
	private String createdTable;
	private String tableIsReady;
	private String doNotHavePermission;
	private String signUp;
	private String reloadMessagesYaml;
	private String reconnect;
	private String closeAllInventory;
	private String inventoryEnabled;
	private String inventoryDisabled;

	// error_messages
	private String failedConncectDb;
	private String failedCreateTable;
	private String failedReadyTable;
	private String failedReconnect;
	private String notEnoughArguments;
	private String failedSignUp;
	private String notPlayer;
	private String notExist;
	private String couldNotOpenInventory;
	private String canNotUse;
	// other
	private String inventoryTitleText;


	private WIMessages () {
		// info_messages
		this.pluginEnabled = resources.getString("pluginEnabled");
		this.pluginDisabled = resources.getString("pluginDisabled");
		this.connectedDb = resources.getString("connectedDb");
		this.disconnectedDb = resources.getString("disconnectedDb");
		this.createdTable = resources.getString("createdTable");
		this.tableIsReady = resources.getString("tableIsReady");
		this.doNotHavePermission = resources.getString("doNotHavePermission");
		this.signUp = resources.getString("signUp");
		this.reloadMessagesYaml = resources.getString("reloadMessagesYaml");
		this.reconnect = resources.getString("reconnect");
		this.closeAllInventory = resources.getString("closeAllInventory");
		this.inventoryEnabled = resources.getString("inventoryEnabled");
		this.inventoryDisabled = resources.getString("inventoryDisabled");
		// error_messages
		this.failedConncectDb = resources.getString("failedConnectDb");
		this.failedCreateTable = resources.getString("failedCreateTable");
		this.failedReadyTable = resources.getString("failedReadyTable");
		this.failedReconnect = resources.getString("failedReconnect");
		this.notEnoughArguments = resources.getString("notEnoughArguments");
		this.failedSignUp = resources.getString("failedSignUp");
		this.notPlayer = resources.getString("notPlayer");
		this.notExist = resources.getString("notExist");
		this.couldNotOpenInventory = resources.getString("couldNotOpenInventory");
		this.canNotUse = resources.getString("canNotUse");
		// other
		this.inventoryTitleText = resources.getString("inventoryTitleText");

		// info_messages
		this.pluginEnabled = Utility.replaceColorCode(this.pluginEnabled);
		this.pluginDisabled = Utility.replaceColorCode(this.pluginDisabled);
		this.connectedDb = Utility.replaceColorCode(this.connectedDb);
		this.createdTable = Utility.replaceColorCode(this.disconnectedDb);
		this.createdTable = Utility.replaceColorCode(this.createdTable);
		this.tableIsReady = Utility.replaceColorCode(this.tableIsReady);
		this.doNotHavePermission = Utility.replaceColorCode(this.doNotHavePermission);
		this.signUp = Utility.replaceColorCode(this.signUp);
		this.reloadMessagesYaml = Utility.replaceColorCode(this.reloadMessagesYaml);
		this.reconnect = Utility.replaceColorCode(this.reconnect);
		this.closeAllInventory = Utility.replaceColorCode(this.closeAllInventory);
		this.inventoryEnabled = Utility.replaceColorCode(this.inventoryEnabled);
		this.inventoryDisabled = Utility.replaceColorCode(this.inventoryDisabled);
		// error_messages
		this.failedConncectDb = Utility.replaceColorCode(this.failedConncectDb);
		this.failedCreateTable = Utility.replaceColorCode(this.failedCreateTable);
		this.failedReadyTable = Utility.replaceColorCode(this.failedReadyTable);
		this.failedReconnect = Utility.replaceColorCode(this.failedReconnect);
		this.notEnoughArguments = Utility.replaceColorCode(this.notEnoughArguments);
		this.failedSignUp = Utility.replaceColorCode(this.failedSignUp);
		this.notPlayer = Utility.replaceColorCode(this.notPlayer);
		this.notExist = Utility.replaceColorCode(this.notExist);
		this.couldNotOpenInventory = Utility.replaceColorCode(this.couldNotOpenInventory);
		this.canNotUse = Utility.replaceColorCode(this.canNotUse);
		// other
		this.inventoryTitleText = Utility.replaceColorCode(this.inventoryTitleText);
	}

	// 初期化
	public static void initialize (WebInventory _plugin, String _fileName) {
		plugin = _plugin;
		fileName = _fileName;
		configFile = new File(plugin.getDataFolder(), fileName);
		saveDefaultYamlPlus(fileName, configFile, true);

		resources = loadUTF8YamlFile(configFile);

		instance = new WIMessages();
	}

	// リロード
	public static void reload () {
		initialize(plugin, fileName);
	}


	// UTF-8エンコードのYamlファイルをロードする
	private static YamlConfiguration loadUTF8YamlFile(File file) {
		YamlConfiguration config = new YamlConfiguration();
		try (Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
			config.load(reader);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return config;
	}

	// jarファイルからymlをコピーする
	public static void saveDefaultYamlPlus (@Nonnull String resourcePath, @Nonnull File outFile, boolean forceUTF8) {
		if (outFile.exists()) {
			return;
		}
		BufferedReader reader = null;
		BufferedWriter writer = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = plugin.getResource(resourcePath);
			if (is == null) {
				return;
			}
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			fos = new FileOutputStream(outFile);
			if (forceUTF8) {
				writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			} else {
				writer = new BufferedWriter(new OutputStreamWriter(fos));
			}

			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
				writer.newLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {

				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {

				}
			}
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {

				}
			}
		}
	}

	public static WIMessages getInstance() {
		return instance;
	}

	public static String getPluginEnabled() {
		return instance.pluginEnabled;
	}

	public static String getPluginDisabled() {
		return instance.pluginDisabled;
	}

	public static String getConnectedDb() {
		return instance.connectedDb;
	}

	public static String getDisconnectedDb() {
		return instance.disconnectedDb;
	}

	public static String getCreatedTable() {
		return instance.createdTable;
	}

	public static String getTableIsReady() {
		return instance.tableIsReady;
	}

	public static String getDoNotHavePermission() {
		return instance.doNotHavePermission;
	}

	public static String getFailedConncectDb() {
		return instance.failedConncectDb;
	}

	public static String getFailedCreateTable() {
		return instance.failedCreateTable;
	}

	public static String getFailedReadyTable() {
		return instance.failedReadyTable;
	}

	public static String getFailedSignUp() {
		return instance.failedSignUp;
	}

	public static String getInventoryTitleText() {
		return instance.inventoryTitleText;
	}

	public static String getSignUp() {
		return instance.signUp;
	}

	public static String getReloadMessagesYaml() {
		return instance.reloadMessagesYaml;
	}

	public static String getNotEnoughArguments() {
		return instance.notEnoughArguments;
	}

	public static String getNotPlayer() {
		return instance.notPlayer;
	}

	public static String getNotExist() {
		return instance.notExist;
	}

	public static String getCouldNotOpenInventory() {
		return instance.couldNotOpenInventory;
	}

	public static String getCloseAllInventory() {
		return instance.closeAllInventory;
	}

	public static String getCanNotUse() {
		return instance.canNotUse;
	}

	public static String getInventoryEnabled() {
		return instance.inventoryEnabled;
	}

	public static String getInventoryDisabled() {
		return instance.inventoryDisabled;
	}

	public static String getReconnect() {
		return instance.reconnect;
	}

	public static String getFailedReconnect() {
		return instance.failedReconnect;
	}



}
