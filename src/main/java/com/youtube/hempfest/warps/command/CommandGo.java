package com.youtube.hempfest.warps.command;

import com.youtube.hempfest.hempcore.HempCore;
import com.youtube.hempfest.hempcore.gui.GuiLibrary;
import com.youtube.hempfest.warps.HempfestWarps;
import com.youtube.hempfest.warps.PrivateWarp;
import com.youtube.hempfest.warps.gui.InventoryHomes;
import com.youtube.hempfest.warps.structure.Warp;
import com.youtube.hempfest.warps.system.WID;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CommandGo extends BukkitCommand {

	public CommandGo() {
		super("go");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {

			return true;
		}

		Player p = (Player) sender;
		int length = args.length;

		if (length == 0) {

			return true;
		}

		if (length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				GuiLibrary lib = HempCore.guiManager(p);
				new InventoryHomes(lib).open();
				return true;
			}
			PrivateWarp warp = new PrivateWarp(args[0], p.getUniqueId());
			if (!PrivateWarp.ownedHomeNames(p.getUniqueId()).contains(args[0])) {
				p.sendMessage(HempfestWarps.getPrefix() + " " +  String.format(HempfestWarps.getString("private-not-found"), args[0]));
				return true;
			}
			if (warp.getOwner().equals(p.getUniqueId().toString())) {
				p.teleport(((Warp) warp).getLocation());
				p.sendMessage(HempfestWarps.getPrefix() + " " +  String.format(HempfestWarps.getString("private-teleport"), args[0]));
				return true;
			}

			return true;
		}

		if (length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				if (PrivateWarp.ownedHomeNames(p.getUniqueId()).contains(args[1])) {
					p.sendMessage(HempfestWarps.getPrefix()+ " " +  String.format(HempfestWarps.getString("private-updated"), args[1]));
					new PrivateWarp(args[1], p.getUniqueId(), WID.randomID()).create();
					return true;
				}
				p.sendMessage(HempfestWarps.getPrefix()+ " " +  String.format(HempfestWarps.getString("private-set"), args[1]));
				new PrivateWarp(args[1], p.getUniqueId(), WID.randomID()).create();
				return true;
			}
			if (args[0].equalsIgnoreCase("delete")) {
				if (!PrivateWarp.ownedHomeNames(p.getUniqueId()).contains(args[0])) {
					p.sendMessage(HempfestWarps.getPrefix() + " " +  String.format(HempfestWarps.getString("private-not-found"), args[0]));
					return true;
				}
				new PrivateWarp(args[1], p.getUniqueId(), WID.randomID()).delete();
				p.sendMessage(HempfestWarps.getPrefix()+ " " +  String.format(HempfestWarps.getString("private-deleted"), args[1]));
				return true;
			}
			return true;
		}

		return true;
	}
}
