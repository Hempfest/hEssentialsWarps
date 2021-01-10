package com.youtube.hempfest.warps;

import com.youtube.hempfest.hempcore.library.HFEncoded;
import com.youtube.hempfest.hempcore.library.HUID;
import com.youtube.hempfest.warps.structure.Warp;
import com.youtube.hempfest.warps.system.Config;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PublicWarp implements Warp {

	private String warpName;

	private Location loc;

	public PublicWarp(String warpName) {
		this.warpName = warpName;
	}

	public PublicWarp(String warpName, Location loc) {
		this.warpName = warpName;
		this.loc = loc;
	}

	@Override
	public Location getLocation() throws IOException, ClassNotFoundException {
		Config main = Config.get(warpName, "Public");
		return (Location) new HFEncoded(main.getConfig().getString("location")).deserialized();
	}

	@Override
	public String getOwner() {
		return Bukkit.getName();
	}

	@Override
	public HUID getId() throws IOException, ClassNotFoundException {
		Config main = Config.get(warpName, "Public");
		return ((HUID) new HFEncoded(main.getConfig().getString("id")).deserialized());
	}

	public void create() throws IOException {
		Config main = Config.get(warpName, "Public");
		main.getConfig().set("location", new HFEncoded(loc).serialize());
		main.getConfig().set("id", new HFEncoded(HUID.randomID()).serialize());
		main.saveConfig();
	}

	public void delete() {
		Config main = Config.get(warpName, "Public");
		if (main.exists()) {
			main.delete();
		}
	}

	public static List<String> allWarps() {
		Config main = Config.get("Test", "Public");
		List<String> array = new ArrayList<>();
		for (File f : Objects.requireNonNull(main.getDataFolder().listFiles())) {
			String name = f.getName().replace(".yml", "");
			if (!array.contains(name)) {
				array.add(name);
			}
		}
		return array;
	}

}
