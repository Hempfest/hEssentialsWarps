package com.youtube.hempfest.warps;

import com.github.sanctum.labyrinth.command.CommandBuilder;
import com.github.sanctum.labyrinth.formatting.string.ColoredString;
import com.github.sanctum.labyrinth.gui.builder.InventoryRows;
import com.github.sanctum.labyrinth.gui.builder.SyncMenuSwitchPageEvent;
import com.youtube.hempfest.warps.system.Config;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class HempfestWarps extends JavaPlugin implements Listener {

	private static HempfestWarps instance;
	private static Config main = Config.get("Config", "Configuration");
	public final HashMap<UUID, Boolean> teleporting = new HashMap<>();
	public MovementCancellation cancellation;

	@Override
	public void onEnable() {
		// Plugin startup logic
		instance = this;
		new CommandBuilder(this).compileFields("com.youtube.hempfest.warps.command");
		Bukkit.getPluginManager().registerEvents(this, this);
		if (getBoolean("cancel-teleport")) {
			cancellation = new MovementCancellation();
			Bukkit.getPluginManager().registerEvents(cancellation, this);
		}
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	private static void pre() {
		if (!main.exists()) {
			InputStream io = getInstance().getResource("Config.yml");
			Config.copy(io, main.getFile());
		}
	}

	public static HempfestWarps getInstance() {
		return instance;
	}

	public static String getPrefix() {
		pre();
		return new ColoredString(main.getConfig().getString("Options.prefix"), ColoredString.ColorType.HEX).toString();
	}

	public static String getString(String path) {
		pre();
		return new ColoredString(main.getConfig().getString("Messages." + path), ColoredString.ColorType.HEX).toString();
	}

	public static String getGuiString(String path) {
		pre();
		return new ColoredString(main.getConfig().getString("Gui." + path), ColoredString.ColorType.HEX).toString();
	}

	public static int getInteger(String path) {
		pre();
		return main.getConfig().getInt("Options." + path);
	}

	public static boolean getBoolean(String path) {
		pre();
		return main.getConfig().getBoolean("Options." + path);
	}

	public static InventoryRows getRows() {
		return InventoryRows.valueOf(getGuiString("menu-rows"));
	}

	public static int getSwitch() {
		int amnt = 0;
		switch (getRows().getSlotCount()) {
			case 9:
				amnt = 5;
				break;
			case 18:
				amnt = 14;
				break;
			case 27:
				amnt = 18;
				break;
			case 36:
				amnt = 27;
				break;
			case 45:
				amnt = 36;
				break;
			case 54:
				amnt = 45;
				break;
		}
		return amnt;
	}

	public static int getBack() {
		int amnt = 0;
		switch (getRows().getSlotCount()) {
			case 9:
				amnt = 7;
				break;
			case 18:
				amnt = 16;
				break;
			case 27:
				amnt = 22;
				break;
			case 36:
				amnt = 31;
				break;
			case 45:
				amnt = 40;
				break;
			case 54:
				amnt = 49;
				break;
		}
		return amnt;
	}

	public static int getRight() {
		int amnt = 0;
		switch (getRows().getSlotCount()) {
			case 9:
				amnt = 8;
				break;
			case 18:
				amnt = 17;
				break;
			case 27:
				amnt = 23;
				break;
			case 36:
				amnt = 32;
				break;
			case 45:
				amnt = 41;
				break;
			case 54:
				amnt = 50;
				break;
		}
		return amnt;
	}

	public static int getLeft() {
		int amnt = 0;
		switch (getRows().getSlotCount()) {
			case 9:
				amnt = 6;
				break;
			case 18:
				amnt = 15;
				break;
			case 27:
				amnt = 21;
				break;
			case 36:
				amnt = 30;
				break;
			case 45:
				amnt = 39;
				break;
			case 54:
				amnt = 48;
				break;
		}
		return amnt;
	}

	public static int getAmntPer() {
		int amnt = 0;
		switch (getRows().getSlotCount()) {
			case 9:
				amnt = 6;
				break;
			case 18:
				amnt = 15;
				break;
			case 27:
				amnt = 7;
				break;
			case 36:
				amnt = 14;
				break;
			case 45:
				amnt = 21;
				break;
			case 54:
				amnt = 28;
				break;
		}
		return amnt;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent e) {
		Config main = Config.get(e.getPlayer().getUniqueId().toString(), "Private");
		if (!main.exists()) {
			main.getConfig().createSection("Owned");
			main.saveConfig();
		}
	}


}
