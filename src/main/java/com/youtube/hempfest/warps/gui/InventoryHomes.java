package com.youtube.hempfest.warps.gui;

import com.youtube.hempfest.hempcore.HempCore;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import com.youtube.hempfest.hempcore.gui.GuiLibrary;
import com.youtube.hempfest.hempcore.gui.Pagination;
import com.youtube.hempfest.hempcore.library.Items;
import com.youtube.hempfest.warps.HempfestWarps;
import com.youtube.hempfest.warps.PrivateWarp;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class InventoryHomes extends Pagination {

	public InventoryHomes(GuiLibrary guiLibrary) {
		super(guiLibrary);
	}

	@Override
	public String getMenuName() {
		return new ColoredString(HempfestWarps.getGuiString("private-list-title"), ColoredString.ColorType.HEX).toString();
	}

	@Override
	public int getSlots() {
		return 54;
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
	Player p = (Player) e.getWhoClicked();
	Material mat = e.getCurrentItem().getType();
		ArrayList<String> homes = new ArrayList<>(PrivateWarp.ownedHomeNames(p.getUniqueId()));
	switch (mat) {
		case BARRIER:
			p.closeInventory();
			break;
		case DARK_OAK_BUTTON:
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")) {
				if (page == 0) {
					p.sendMessage(ChatColor.GRAY + "You are already on the first page.");
				} else {
					page = page - 1;
					super.open();
				}
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())
					.equalsIgnoreCase("Right")) {
				if (!((index + 1) >= homes.size())) {
					page = page + 1;
					super.open();
				} else {
					p.sendMessage(ChatColor.GRAY + "You are on the last page.");
				}
			}
			break;
	}
		Material select = Items.getMaterial(HempfestWarps.getGuiString("private-list-icon"));
		if (select == null) {
			select = Material.PAPER;
		}
		String home = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(HempCore.getInstance(), "home"), PersistentDataType.STRING);
	if (mat.equals(select)) {
		Bukkit.dispatchCommand(p, "go " + home);
	}
	}

	@Override
	public void setMenuItems() {
		addMenuBorder();
		ArrayList<String> homes = new ArrayList<>(PrivateWarp.ownedHomeNames(guiLibrary.getUUID()));
		if (homes != null && !homes.isEmpty()) {
			for (int i = 0; i < getMaxItemsPerPage(); i++) {
				index = getMaxItemsPerPage() * page + i;
				if (index >= homes.size())
					break;
				if (homes.get(index) != null) {
					Material mat = Items.getMaterial(HempfestWarps.getGuiString("private-list-icon"));
					if (mat == null) {
						mat = Material.PAPER;
					}
				ItemStack home = makePersistentItem(mat, "&b&oGoto &f&o" + homes.get(index), "home", homes.get(index), "", "&7&oClick to warp.");
				inventory.addItem(home);
				}
			}
		}
		setFillerGlassLight();
	}
}
