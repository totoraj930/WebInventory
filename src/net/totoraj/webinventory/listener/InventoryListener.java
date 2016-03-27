package net.totoraj.webinventory.listener;

import net.totoraj.webinventory.WebInventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener{
	private WebInventory plugin;

	public InventoryListener (WebInventory plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClose (InventoryCloseEvent event) {
		Player viewer = (Player) event.getPlayer();
		plugin.INV_MANAGER.closeInventory(viewer.getUniqueId(), event.getInventory());
	}

}
