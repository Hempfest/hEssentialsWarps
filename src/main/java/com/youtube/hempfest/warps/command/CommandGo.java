package com.youtube.hempfest.warps.command;

import com.youtube.hempfest.hempcore.HempCore;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import com.youtube.hempfest.hempcore.formatting.string.PaginatedAssortment;
import com.youtube.hempfest.hempcore.gui.GuiLibrary;
import com.youtube.hempfest.hempcore.library.HFEncoded;
import com.youtube.hempfest.hempcore.library.HUID;
import com.youtube.hempfest.warps.HempfestWarps;
import com.youtube.hempfest.warps.PrivateWarp;
import com.youtube.hempfest.warps.gui.InventoryHomes;
import com.youtube.hempfest.warps.structure.Warp;
import com.youtube.hempfest.warps.system.Config;
import com.youtube.hempfest.warps.system.HomeInheritance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CommandGo extends BukkitCommand {

	public CommandGo() {
		super("go");
		setAliases(Arrays.asList("hgo", "home"));
	}



	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {

			return true;
		}

		Player p = (Player) sender;
		int length = args.length;

		if (length == 0) {
			p.sendMessage(" ");
			List<String> help = new ArrayList<>(Arrays.asList("&8&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "&8&m| |:: &7/&f" + commandLabel + " &7list - View all your current warps", "&8&m| |:: &7/&f" + commandLabel + " &7set <&8warpName&7> - Create a warp in the location you stand.", "&8&m| |:: &7/&f" + commandLabel + " &7delete <&8warpName&7> - Delete a warp you currently own.", "&8&m| |:: &7/&f" + commandLabel + " &7list <&8warpName&7> - List all the users you currently share a warp with.", "&8&m| |:: &7/&f" + commandLabel + " &7add <&8playerName&7> <&8warpName&7> - Share a warp with another player.", "&8&m| |:: &7/&f" + commandLabel + " &7remove <&8playerName&7> <&8warpName&7> - Take access of your home away from a player."));
			PaginatedAssortment assortment = new PaginatedAssortment(p, help);
			assortment.setListTitle("&3&oPrivate warp &f&lCOMMAND &3&ohelp. &8»");
			assortment.setListBorder("&8&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			assortment.setLinesPerPage(7);
			assortment.setNavigateCommand("help");
			assortment.export(1);
			return true;
		}

		if (length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (p.hasPermission("hwarps.reload")) {
					Config main = new Config("Config", "Configuration");
					main.reload();
					p.sendMessage(HempfestWarps.getPrefix() + " " + new ColoredString("&b&oConfiguration v&f" + main.getConfig().getString("Version") + " &b&oreloaded.", ColoredString.ColorType.HEX).toString());
					return true;
				}
				return false;
			}
			if (args[0].equalsIgnoreCase("list")) {
				GuiLibrary lib = HempCore.guiManager(p);
				new InventoryHomes(lib).open();
				return true;
			}
			PrivateWarp warp = new PrivateWarp(args[0], p.getUniqueId());
			try {
				if (!PrivateWarp.ownedHomeNames(p.getUniqueId()).contains(args[0]) && !PrivateWarp.sharedHomeNames(p.getUniqueId()).contains(args[0])) {
					p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-not-found"), args[0]));
					return true;
				}
			} catch (NullPointerException e) {
				p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-not-found"), args[0]));
				return true;
			}
			try {
				if (PrivateWarp.sharedHomeNames(p.getUniqueId()).contains(args[0])) {
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
			try {
				if (p.isOp()) {
					p.sendMessage(((Warp) warp).getId().toString() + " Is this warps special ID.");
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			p.sendMessage(HempfestWarps.getPrefix() + " " +  String.format(HempfestWarps.getString("private-teleport"), args[0]));

			return true;
		}

		if (length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				try {
					if (PrivateWarp.ownedHomeNames(p.getUniqueId()).contains(args[1])) {
						try {
							new PrivateWarp(args[1], p.getUniqueId(), HUID.randomID()).create();
							p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-updated"), args[1]));
						} catch (IOException e) {
							e.printStackTrace();
						}
						return true;
					}
				} catch (NullPointerException e) {
					try {
						if (PrivateWarp.ownedHomeNames(p.getUniqueId()).size() >= PrivateWarp.maxWarps(p)) {
							p.sendMessage(HempfestWarps.getPrefix()+ " "  + new ColoredString("&c&oYour max home limit has been reached. Current: &f" + PrivateWarp.ownedHomeNames(p.getUniqueId()).size(), ColoredString.ColorType.HEX).toString());
							return true;
						}
						new PrivateWarp(args[1], p.getUniqueId(), HUID.randomID()).create();
						p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-set"), args[1]));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					return true;
				}
				p.sendMessage(HempfestWarps.getPrefix()+ " " +  String.format(HempfestWarps.getString("private-set"), args[1]));
				try {
					if (PrivateWarp.ownedHomeNames(p.getUniqueId()).size() >= PrivateWarp.maxWarps(p)) {
						p.sendMessage(HempfestWarps.getPrefix()+ " "  + new ColoredString("&c&oYour max home limit has been reached. Current: &f" + PrivateWarp.maxWarps(p), ColoredString.ColorType.HEX).toString());
						return true;
					}
					new PrivateWarp(args[1], p.getUniqueId(), HUID.randomID()).create();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("delete")) {
				try {
					if (!PrivateWarp.ownedHomeNames(p.getUniqueId()).contains(args[1])) {
						p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-not-found"), args[1]));
						return true;
					}
				} catch (NullPointerException e) {
					p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-not-found"), args[1]));
					return true;
				}
				new PrivateWarp(args[1], p.getUniqueId()).delete();
				p.sendMessage(HempfestWarps.getPrefix()+ " " +  String.format(HempfestWarps.getString("private-deleted"), args[1]));
				return true;
			}
			if (args[0].equalsIgnoreCase("list")) {
				String warp = args[1];
				PrivateWarp home = new PrivateWarp(warp, p.getUniqueId());
				Config main = new Config(p.getUniqueId().toString(), "Private");
				if (!PrivateWarp.ownedHomeNames(p.getUniqueId()).contains(warp)) {
					p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-not-found"), args[1]));
					return true;
				}
				try {
					HomeInheritance hi = (HomeInheritance) new HFEncoded(main.getConfig().getString("Shared." + home.getId())).deserialized();
					p.sendMessage(HempfestWarps.getPrefix()+ " "  + new ColoredString("&6&oCurrent users you share &f&l" + warp + " &6&owith: &r&o" + hi.getUsers().toString(), ColoredString.ColorType.HEX).toString());
				} catch (NullPointerException | IOException | ClassNotFoundException e) {
					p.sendMessage(HempfestWarps.getPrefix()+ " "  + new ColoredString("&c&oYou either don't own this warp or it doesn't exist.", ColoredString.ColorType.HEX).toString());
				}
			}
			return true;
		}

		if (length == 3) {
			if (args[0].equalsIgnoreCase("add")) {
				String player = args[1];
				String warp = args[2];
				PrivateWarp home = new PrivateWarp(warp, p.getUniqueId());
				Config main = new Config(p.getUniqueId().toString(), "Private");
				try {
					if (!PrivateWarp.ownedHomeNames(p.getUniqueId()).contains(warp)) {
						p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-not-found"), args[0]));
						return true;
					}
					HomeInheritance hi = (HomeInheritance) new HFEncoded(main.getConfig().getString("Shared." + home.getId())).deserialized();
					if (hi.getUsers().contains(player)) {
						p.sendMessage(HempfestWarps.getPrefix()+ " "  + new ColoredString("&a&oPlayer already has access to this warp.", ColoredString.ColorType.HEX).toString());
						return true;
					}
					Player target = Bukkit.getPlayer(player);
					if (target != null) {
						PrivateWarp targetClone = new PrivateWarp(warp, target.getUniqueId(), home.getId());
						targetClone.create(home.getLocation());
						hi.add(target.getUniqueId());
						Bukkit.getScheduler().scheduleSyncDelayedTask(HempfestWarps.getInstance(), () -> {
							try {
								hi.commit(main);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}, 1);
						p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-shared"), warp, player));
					} else {
						p.sendMessage(HempfestWarps.getPrefix()+ " "  + new ColoredString("&c&oTarget not found.", ColoredString.ColorType.HEX).toString());
					}
				} catch (NullPointerException | IOException | ClassNotFoundException e) {
					try {
						HomeInheritance homeInheritance = new HomeInheritance(home.getId());
						Player target = Bukkit.getPlayer(player);
						homeInheritance.add(Bukkit.getPlayer(player).getUniqueId());
						if (target != null) {
							PrivateWarp targetClone = new PrivateWarp(warp, target.getUniqueId(), home.getId());
							targetClone.create(home.getLocation());
							homeInheritance.add(target.getUniqueId());
							Bukkit.getScheduler().scheduleSyncDelayedTask(HempfestWarps.getInstance(), () -> {
								try {
									homeInheritance.commit(main);
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}, 1);
							p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-shared"), warp, player));
						} else {
							p.sendMessage(HempfestWarps.getPrefix()+ " "  + new ColoredString("&c&oTarget not found.", ColoredString.ColorType.HEX).toString());
						}
					} catch (IOException | ClassNotFoundException ioException) {
						ioException.printStackTrace();
					}
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("remove")) {
				String player = args[1];
				String warp = args[2];
				PrivateWarp home = new PrivateWarp(warp, p.getUniqueId());
				Config main = new Config(p.getUniqueId().toString(), "Private");
				try {
					if (!PrivateWarp.ownedHomeNames(p.getUniqueId()).contains(warp)) {
						p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-not-found"), args[0]));
						return true;
					}
					HomeInheritance hi = (HomeInheritance) new HFEncoded(main.getConfig().getString("Shared." + home.getId())).deserialized();
					if (!hi.getUsers().contains(player)) {
						p.sendMessage(HempfestWarps.getPrefix()+ " "  + new ColoredString("&c&oThis player does not currently share this warp.", ColoredString.ColorType.HEX).toString());

						return true;
					}
					Player target = Bukkit.getPlayer(player);
					if (target != null) {
						Config targetC = new Config(target.getUniqueId().toString(), "Private");
						targetC.getConfig().set("Shared." + warp, null);
						targetC.saveConfig();
						hi.remove(target.getUniqueId());
						Bukkit.getScheduler().scheduleSyncDelayedTask(HempfestWarps.getInstance(), () -> {
							try {
								hi.commit(main);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}, 1);
						p.sendMessage(HempfestWarps.getPrefix() + " " + String.format(HempfestWarps.getString("private-taken"), warp, player));
					} else {
						p.sendMessage(HempfestWarps.getPrefix()+ " "  + new ColoredString("&c&oTarget not found.", ColoredString.ColorType.HEX).toString());
					}
				} catch (NullPointerException | IOException | ClassNotFoundException e) {
					p.sendMessage(HempfestWarps.getPrefix()+ " "  + new ColoredString("&c&oYou do not share this warp with anyone currently.", ColoredString.ColorType.HEX).toString());
				}
				return true;
			}
			return true;
		}
		// unknown sub command
		return true;
		}
	}
