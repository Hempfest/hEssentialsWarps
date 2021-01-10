package com.youtube.hempfest.warps;

import com.youtube.hempfest.hempcore.gui.Pagination;
import com.youtube.hempfest.hempcore.library.HFEncoded;
import com.youtube.hempfest.hempcore.library.HUID;
import com.youtube.hempfest.warps.structure.Warp;
import com.youtube.hempfest.warps.system.Config;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class PrivateWarp implements Warp {

	private UUID ownerID;

	private HUID warpId;

	private final String warpName;

	public PrivateWarp (String warpName, UUID ownerID) {
		this.warpName = warpName;
		this.ownerID = ownerID;
	}

	public PrivateWarp(String warpName, UUID ownerId, HUID warpId) {
		this.ownerID = ownerId;
		this.warpId = warpId;
		this.warpName = warpName;
	}

	@Override
	public Location getLocation() throws IOException, ClassNotFoundException {
		Config main = Config.get(getOwner(), "Private");
		return ((Location) new HFEncoded(main.getConfig().getString("Owned." + warpName + ".location")).deserialized());
	}

	public Location getSharedLocation() throws IOException, ClassNotFoundException {
		Config main = Config.get(ownerID.toString(), "Private");
		return ((Location) new HFEncoded(main.getConfig().getString("Shared." + warpName + ".location")).deserialized());
	}

	@Override
	public String getOwner() {
		String result = null;
		for (String pID : allPlayers()) {
			for (String o : ownedHomeNames(UUID.fromString(pID))) {
				if (o.equals(warpName)) {
					result = pID;
					break;
				}
			}
		}
		return result;
	}

	public boolean isOwner() {
		return ownedHomeNames(ownerID).contains(warpName);
	}

	@Override
	public HUID getId() throws IOException, ClassNotFoundException {
		Config main = Config.get(getOwner(), "Private");
		return (HUID) new HFEncoded(main.getConfig().getString("Owned." + warpName + ".id")).deserialized();
	}

	public void create() throws IOException {
		Config main = Config.get(ownerID.toString(), "Private");
		main.getConfig().set("Owned." + warpName + ".location", new HFEncoded(Bukkit.getPlayer(ownerID).getLocation()).serialize());
		main.getConfig().set("Owned." + warpName + ".id", new HFEncoded(warpId).serialize());
		main.saveConfig();
	}

	public void create(Location toCopy) throws IOException {
		Bukkit.getLogger().info("Creation dupe warp for warp " + warpName + " for player " + Bukkit.getOfflinePlayer(ownerID).getName());
		Config main = Config.get(ownerID.toString(), "Private");
		main.getConfig().set("Shared." + warpName + ".location", new HFEncoded(toCopy).serialize());
		main.getConfig().set("Shared." + warpName + ".id", new HFEncoded(warpId).serialize());
		main.saveConfig();
	}

	public void delete() {
		if (ownedHomeNames(ownerID).contains(warpName)) {
			Config main = Config.get(ownerID.toString(), "Private");
			main.getConfig().set("Owned." + warpName, null);
			main.saveConfig();
		}
	}

	public static List<String> ownedHomeNames(UUID playerId) {
		Config main = Config.get(playerId.toString(), "Private");
		List<String> array = new ArrayList<>();
		for (String o : main.getConfig().getConfigurationSection("Owned").getKeys(false)) {
			if(!array.contains(o)) {
				array.add(o);
			}
		}
		return array;
	}

	public static List<String> sharedHomeNames(UUID playerId) {
		Config main = Config.get(playerId.toString(), "Private");
		List<String> array = new ArrayList<>();
		for (String o : main.getConfig().getConfigurationSection("Shared").getKeys(false)) {
			if (main.getConfig().isConfigurationSection("Shared." + o)) {
				if(!array.contains(o)) {
					array.add(o);
				}
			}
		}
		return array;
	}

	public static List<String> allPlayers() {
		List<String> array = new ArrayList<>();
		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			array.add(player.getUniqueId().toString());
		}
		return array;
	}

	public static int maxWarps(Player p) {
		int result = 0;
		List<String> identifiers = new ArrayList<>(Arrays.asList("hessentials.homes", "hwarps.homes"));
		for (int i = 1; i < 251; i++) {
			for (String identifier : identifiers) {
				if (p.hasPermission(identifier + ".infinite")) {
					result = -1;
					break;
				}
				if (p.hasPermission(identifier + "." + i)) {
					result = i;
				}
			}
		}
		return result == -1 ? 99999 : result;
	}

}
