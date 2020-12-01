package com.youtube.hempfest.warps;

import com.youtube.hempfest.warps.structure.Warp;
import com.youtube.hempfest.warps.system.Config;
import com.youtube.hempfest.warps.system.WID;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PrivateWarp implements Warp {

	private UUID ownerID;

	private WID warpId;

	private final String warpName;

	public PrivateWarp (String warpName, UUID ownerID) {
		this.warpName = warpName;
		this.ownerID = ownerID;
	}

	public PrivateWarp(String warpName, UUID ownerId, WID warpId) {
		this.ownerID = ownerId;
		this.warpId = warpId;
		this.warpName = warpName;
	}

	@Override
	public Location getLocation() {
		Config main = new Config(getOwner(), "Private");
		return main.getConfig().getLocation("Owned." + warpName + ".location");
	}

	@Override
	public String getOwner() {
		String result = null;
		for (String pID : allPlayers()) {
			Config main = new Config(pID, "Private");
			if (main.getConfig().getConfigurationSection("Owned").getKeys(false).contains(warpName)) {
				result = pID;
			}
		}
		return result;
	}

	public boolean isOwner() {
		return ownedHomeNames(ownerID).contains(warpName);
	}

	@Override
	public WID getId() {
		Config main = new Config(getOwner(), "Private");
		return WID.fromString(main.getConfig().getString("Owned." + warpName + ".id"));
	}

	public void create() {
		Config main = new Config(ownerID.toString(), "Private");
		main.getConfig().set("Owned." + warpName + ".location", Bukkit.getPlayer(ownerID).getLocation());
		main.getConfig().set("Owned." + warpName + ".id", warpId.toString());
		main.saveConfig();
	}

	public void delete() {
		if (ownedHomeNames(ownerID).contains(warpName)) {
			Config main = new Config(ownerID.toString(), "Private");
			main.getConfig().set("Owned." + warpName, null);
			main.saveConfig();
		}
	}

	public static List<String> ownedHomeNames(UUID playerId) {
		Config main = new Config(playerId.toString(), "Private");
		List<String> array = new ArrayList<>();
		for (String o : main.getConfig().getConfigurationSection("Owned").getKeys(false)) {
			if(!array.contains(o)) {
				array.add(o);
			}
		}
		return array;
	}

	public static List<String> allPlayers() {
		Config main = new Config(null, "Private");
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
