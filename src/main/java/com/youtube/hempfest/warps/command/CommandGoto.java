package com.youtube.hempfest.warps.command;

import com.youtube.hempfest.warps.HempfestWarps;
import com.youtube.hempfest.warps.PrivateWarp;
import com.youtube.hempfest.warps.structure.Warp;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CommandGoto extends BukkitCommand {

	public CommandGoto() {
		super("goto");
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

		if (length == 2) {
			OfflinePlayer target = null;
			for (String play : PrivateWarp.allPlayers()) {
				if (Bukkit.getOfflinePlayer(UUID.fromString(play)).getName().equals(args[0])) {
					target = Bukkit.getOfflinePlayer(UUID.fromString(play));
					break;
				}
			}
			if (target == null) {

				return true;
			}
			PrivateWarp warp = new PrivateWarp(args[1], target.getUniqueId());
			try {
				if (!PrivateWarp.ownedHomeNames(target.getUniqueId()).contains(args[1]) && !PrivateWarp.sharedHomeNames(target.getUniqueId()).contains(args[1])) {
					p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-not-found"), args[1]));
					return true;
				}
			} catch (NullPointerException e) {
				p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-not-found"), args[1]));
				return true;
			}
			try {
				if (PrivateWarp.sharedHomeNames(target.getUniqueId()).contains(args[1])) {
					p.teleport(warp.getSharedLocation());
				} else {
					p.teleport(((Warp) warp).getLocation());
				}
			} catch (NullPointerException | IOException | ClassNotFoundException e) {
				try {
					p.teleport(((Warp) warp).getLocation());
				} catch (IOException | ClassNotFoundException ioException) {
					ioException.printStackTrace();
				}
			}
			p.sendMessage(HempfestWarps.getPrefix() + " " +  String.format(HempfestWarps.getString("private-teleport"), args[1]));
		}


		return true;
	}
}
