package net.totoraj.webinventory;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class InventoryManager {
	private WebInventory plugin;
	private DataManager datamanager;
	private HashMap<UUID, Inventory> inventorys;
	private HashMap<UUID, UUID> viewers;

	public InventoryManager (WebInventory plugin) {
		this.plugin = plugin;
		datamanager = plugin.DATA_MANAGER;
		inventorys = new HashMap<UUID, Inventory>();
		viewers = new HashMap<UUID, UUID>();
	}

	// インベントリを開くメソッド
	public boolean openInventory (UUID viewerUUID, UUID targetUUID) {
		Inventory inv;
		// 既にインベントリが開かれていればそのまま使う
		if (inventorys.containsKey(targetUUID)) {
			inv = inventorys.get(targetUUID);
		}
		// インベントリがなければ取得
		else {
			inv = datamanager.getInventory(targetUUID);
			if (inv == null) {
				return false;
			}
			plugin.getServer().getPlayer(viewerUUID).openInventory(inv);
		}

		// インベントリを開く
		plugin.getServer().getPlayer(viewerUUID).openInventory(inv);

		// プレイヤーをマップに追加
		viewers.put(viewerUUID, targetUUID);
		// インベントリをマップに追加
		inventorys.put(targetUUID, inv);
		return true;
	}


	// インベントリが閉じられたときに呼ぶメソッド
	public boolean closeInventory (UUID viewerUUID, Inventory targetInventory) {

		if (!viewers.containsKey(viewerUUID)) {
			return false;
		}
		if (!inventorys.containsValue(targetInventory)) {
			return false;
		}

		// インベントリの所有者のUUIDを取得
		UUID targetUUID = viewers.get(viewerUUID);

		// インベントリをデータベースに保存
		plugin.DATA_MANAGER.setInventory(targetUUID, targetInventory);
		// プレイヤーをマップから削除
		viewers.remove(viewerUUID);

		// すでにインベントリを開いているプレイヤーがいなければマップからインベントリを削除
		if (!viewers.containsValue(targetUUID)) {
			inventorys.remove(targetUUID);
		}

		return true;
	}

	// 開かれているWebInventoryを閉じる
	public void closeAllInventory () {
		for (UUID viewerUUID : viewers.keySet()) {
			Player viewer = plugin.getServer().getPlayer(viewerUUID);
			if (viewer == null) {
				continue;
			}
			viewer.closeInventory();
		}
	}

}
