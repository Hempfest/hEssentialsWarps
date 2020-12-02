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
import org.bukkit.event.player.PlayerJoinEvent;
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

	@EventHandler(priority = EventPriority.HIGH)
	public void joinLog(PlayerJoinEvent e) {
		Config main = new Config(e.getPlayer().getUniqueId().toString(), "Private");
		if (!main.exists()) {
			main.getConfig().createSection("Owned");
			main.saveConfig();
		}
	}


}
