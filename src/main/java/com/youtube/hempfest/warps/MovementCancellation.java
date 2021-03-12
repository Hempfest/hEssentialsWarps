package com.youtube.hempfest.warps;

import com.github.sanctum.labyrinth.library.StringUtils;
import com.github.sanctum.labyrinth.task.Schedule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementCancellation implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onMove(PlayerMoveEvent e) {
		if (e.getTo() != null) {
			if (e.getFrom().getX() != e.getTo().getX() && e.getFrom().getY() != e.getTo().getY() && e.getFrom().getZ() != e.getTo().getZ())
				Schedule.async(() -> {
					if (HempfestWarps.getInstance().teleporting.containsKey(e.getPlayer().getUniqueId())) {
						HempfestWarps.getInstance().teleporting.remove(e.getPlayer().getUniqueId());
						e.getPlayer().sendMessage(HempfestWarps.getPrefix() + StringUtils.translate(" &c&oYou moved! Teleport has been cancelled."));
					}
				}).debug().run();
		}
	}

}
