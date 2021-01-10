package com.youtube.hempfest.warps.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CommandShowWarps extends BukkitCommand {

	public CommandShowWarps() {
		super("showhomes");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {

			return true;
		}

		Player p = (Player) sender;
		int length = args.length;

		if (length == 0) {
			// not enough args
			return true;
		}

		if (length == 1) {

			return true;
		}


		return true;
	}
}
