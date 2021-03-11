package com.youtube.hempfest.warps.command;

import com.youtube.hempfest.warps.gui.GUI;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CommandWarps extends BukkitCommand {

	public CommandWarps() {
		super("warps");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {

			return true;
		}

		Player p = (Player) sender;
		int length = args.length;

		if (length == 0) {
			GUI.select(GUI.MenuType.WARPS).open(p);
			return true;
		}

		return true;
	}
}
