package com.youtube.hempfest.warps.command;

import com.youtube.hempfest.warps.PrivateWarp;
import com.youtube.hempfest.warps.gui.GUI;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CommandShowWarps extends BukkitCommand {

	public CommandShowWarps() {
		super("showhomes");
		setPermission("hwarps.warp.other");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {

			return true;
		}

		Player p = (Player) sender;
		int length = args.length;

		if (!p.hasPermission(this.getPermission())) {

			return true;
		}

		if (length == 0) {
			// not enough args
			return true;
		}

		if (length == 1) {
			OfflinePlayer target = null;
			for (String play : PrivateWarp.allPlayers()) {
				if (Bukkit.getOfflinePlayer(UUID.fromString(play)).getName().equals(args[0])) {
					target = Bukkit.getOfflinePlayer(UUID.fromString(play));
					break;
				}
			}
			if (target == null) {
				// player not found
				return true;
			}
			GUI.select(GUI.MenuType.HOMES_OTHER, target.getUniqueId()).open(p);
			return true;
		}


		return true;
	}
}
