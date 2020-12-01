package com.youtube.hempfest.warps;

import com.youtube.hempfest.hempcore.command.CommandBuilder;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import com.youtube.hempfest.hempcore.gui.Menu;
import com.youtube.hempfest.warps.system.Config;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.java.JavaPlugin;

public final class HempfestWarps extends JavaPlugin implements Listener {

	private static HempfestWarps instance;
	private static Config main = new Config("Config", "Configuration");
	public static HashMap<UUID, Boolean> teleporting = new HashMap<>();

	@Override
	public void onEnable() {
		// Plugin startup logic
		instance = this;
		new CommandBuilder(this).compileFields("com.youtube.hempfest.warps.command");
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	private static void run() {
		if (!main.exists()) {
			InputStream io = getInstance().getResource("Config.yml");
			Config.copy(io, main.getFile());
		}
	}

	public static HempfestWarps getInstance() {
		return instance;
	}

	public static String getPrefix() {
		run();
		return new ColoredString(main.getConfig().getString("Options.prefix"), ColoredString.ColorType.HEX).toString();
	}

	public static String getString(String path) {
		run();
		return new ColoredString(main.getConfig().getString("Messages." + path), ColoredString.ColorType.HEX).toString();
	}

	public static String getGuiString(String path) {
		run();
		return new ColoredString(main.getConfig().getString("Gui." + path), ColoredString.ColorType.HEX).toString();
	}

	// GUI interact event
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onMenuClick(InventoryClickEvent e) {

		InventoryHolder holder = e.getInventory().getHolder();
		// If the inventory holder of the inventory clicked on
		// is an instance of Menu, then gg. The reason that
		// an InventoryHolder can be a Menu is because our Menu
		// class implements InventoryHolder!!
		try {
			if (holder instanceof Menu) {
				e.setCancelled(true); // prevent them from fucking with the inventory
				if (e.getCurrentItem() == null) { // deal with null exceptions
					return;
				}
				// Since we know our inventory holder is a menu, get the Menu Object representing
				// the menu we clicked on
				Menu menu = (Menu) holder;
				// Call the handleMenu object which takes the event and processes it
				menu.handleMenu(e);
			}
		} catch (AuthorNagException ignored) {
		}
	}


}
