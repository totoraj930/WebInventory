package net.totoraj.webinventory;

import org.bukkit.ChatColor;

public class Utility {

	// 文字列内のカラーコードを置き換えして返す
	public static String replaceColorCode(String source) {
		if (source == null)
			return null;
		return ChatColor.translateAlternateColorCodes('&', source);
	}
}
