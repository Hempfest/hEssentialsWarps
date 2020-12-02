package com.youtube.hempfest.warps.command;

import com.youtube.hempfest.hempcore.HempCore;
import com.youtube.hempfest.hempcore.gui.GuiLibrary;
import com.youtube.hempfest.warps.gui.InventoryWarps;
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
			GuiLibrary library = HempCore.guiManager(p);
			new InventoryWarps(library).open();
			return true;
		}

		return true;
	}
}
