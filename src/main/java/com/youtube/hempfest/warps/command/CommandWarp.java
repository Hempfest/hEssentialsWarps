package com.youtube.hempfest.warps.command;

import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import com.youtube.hempfest.warps.HempfestWarps;
import com.youtube.hempfest.warps.PublicWarp;
import com.youtube.hempfest.warps.structure.Warp;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

public class CommandWarp extends BukkitCommand {

	public CommandWarp() {
		super("warp");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {

			return true;
		}

		Player p = (Player) sender;
		int length = args.length;

		if (length == 0) {
			p.sendMessage(HempfestWarps.getPrefix() + new ColoredString(" &c&oUsage: &8/&7" + commandLabel + " &8<&7warpName&8>", ColoredString.ColorType.HEX).toString());
			return true;
		}

		if (length == 1) {
			if (args[0].equalsIgnoreCase("delete")) {
				if (p.hasPermission("hwarps.delete")) {
					p.sendMessage(HempfestWarps.getPrefix() + new ColoredString("&c&oUsage: &8/&7" + commandLabel + " &8delete <&7warpName&8>", ColoredString.ColorType.HEX).toString());
					return true;
				}
				return false;
			}
			if (args[0].equalsIgnoreCase("set")) {
				if (p.hasPermission("hwarps.set")) {
					p.sendMessage(HempfestWarps.getPrefix() + new ColoredString("&c&oUsage: &8/&7" + commandLabel + " &8set <&7warpName&8>", ColoredString.ColorType.HEX).toString());
					return true;
				}
				return false;
			}
			if (PublicWarp.allWarps().contains(args[0])) {
				if (!p.hasPermission("hwarps.warp." + args[0])) {
					p.sendMessage(HempfestWarps.getPrefix() + new ColoredString(" &c&oYou do not have access to this warp!", ColoredString.ColorType.HEX).toString());
					return true;
				}
				Warp warp = new PublicWarp(args[0]);
				try {
					p.sendMessage(HempfestWarps.getPrefix() + String.format(HempfestWarps.getString("public-teleport"), args[0]));
					p.teleport(warp.getLocation());
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				return true;
			} else
				p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("public-not-found"), args[0]));
			return true;
		}

		if (length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				try {
					if (p.hasPermission("hwarps.set")) {
						if (PublicWarp.allWarps().contains(args[1])) {
							new PublicWarp(args[1], p.getLocation()).create();
							Bukkit.broadcastMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("public-updated"), args[1]));
							return true;
						}
						new PublicWarp(args[1], p.getLocation()).create();
						Bukkit.broadcastMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("public-set"), args[1]));
					} else {
						return false;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("delete")) {
				if (p.hasPermission("hwarps.delete")) {
					new PublicWarp(args[1], p.getLocation()).delete();
					Bukkit.broadcastMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("public-deleted"), args[1]));
				} else {
					return false;
				}
				return true;
			}
			return true;
		}

		return true;
	}
}
