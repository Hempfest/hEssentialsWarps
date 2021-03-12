package com.youtube.hempfest.warps.structure;

import com.github.sanctum.labyrinth.formatting.string.ColoredString;
import com.github.sanctum.labyrinth.library.HUID;
import com.github.sanctum.labyrinth.library.StringUtils;
import com.github.sanctum.labyrinth.task.Schedule;
import com.youtube.hempfest.warps.HempfestWarps;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface Warp {

	Location getLocation() throws IOException, ClassNotFoundException;

	default void teleport(Player p, WarpType type, String warpName, Location location) {
		int entCount = 0;
		for (Entity e : p.getNearbyEntities(HempfestWarps.getInteger("teleport-distance"), HempfestWarps.getInteger("teleport-distance"), HempfestWarps.getInteger("teleport-distance"))) {
			if (e instanceof Player) {
				entCount++;
			}
		}
		if (entCount > 0) {
			if (!HempfestWarps.getInstance().teleporting.containsKey(p.getUniqueId())) {
				p.sendMessage(HempfestWarps.getPrefix() + " " + new ColoredString("&c&oSomeone is nearby. Warping in " + HempfestWarps.getInteger("teleport-delay") + " seconds.", ColoredString.ColorType.MC).toString());
				if (HempfestWarps.getBoolean("cancel-teleport")) {
					p.sendMessage(HempfestWarps.getPrefix() + StringUtils.translate("&cStand still to teleport or be faced with cancellation."));
				}
				HempfestWarps.getInstance().teleporting.putIfAbsent(p.getUniqueId(), true);
				Schedule.sync(() -> {
					switch (type) {
						case PUBLIC:
							p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("public-teleport"), warpName));
							break;
						case PRIVATE:
							p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-teleport"), warpName));
							break;
					}
					p.teleport(location);
					HempfestWarps.getInstance().teleporting.remove(p.getUniqueId());
				}).cancelAbsence(HempfestWarps.getInstance().teleporting, p.getUniqueId()).wait(20 * HempfestWarps.getInteger("teleport-delay"));
			} else {
				p.sendMessage(HempfestWarps.getPrefix() + StringUtils.translate(" &cYou already have a pending teleport!"));
			}
		} else {
			switch (type) {
				case PUBLIC:
					p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("public-teleport"), warpName));
					break;
				case PRIVATE:
					p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-teleport"), warpName));
					break;
			}
			p.teleport(location);
		}
	}

	String getOwner();

	HUID getId() throws IOException, ClassNotFoundException;

}
