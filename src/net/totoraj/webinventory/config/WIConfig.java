package net.totoraj.webinventory.config;

import java.io.File;
import java.io.IOException;

import net.totoraj.webinventory.WebInventory;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class WIConfig {
	private WebInventory plugin;
	private YamlConfiguration yaml;
	private File yamlFile;
	private String yamlName;

	public WIConfig (WebInventory plugin, String yamlName) {
		this.plugin = plugin;
		this.yamlName = yamlName;
		yaml = new YamlConfiguration();
		yamlFile = new File(plugin.getDataFolder(), yamlName);
	}

	// Yamlを返す
	public YamlConfiguration getYaml () {
		return yaml;
	}

	// Yamlを読み込む
	public boolean loadYaml () {
		try {
			yaml.load(yamlFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Yamlを保存する
	public boolean saveYaml () {
		try {
			yaml.save(yamlFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 初期のYamlを保存する
	public boolean saveDefaultYaml () {
		if (yamlFile.exists()) {
			return false;
		}
		plugin.saveResource(yamlName, false);
		return true;
	}
}
