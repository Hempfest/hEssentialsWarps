package com.youtube.hempfest.warps.gui;

import com.github.sanctum.labyrinth.gui.builder.PaginatedBuilder;
import com.github.sanctum.labyrinth.gui.builder.PaginatedClick;
import com.github.sanctum.labyrinth.gui.builder.PaginatedClose;
import com.github.sanctum.labyrinth.gui.builder.PaginatedMenu;
import com.github.sanctum.labyrinth.library.Items;
import com.github.sanctum.labyrinth.library.StringUtils;
import com.youtube.hempfest.warps.HempfestWarps;
import com.youtube.hempfest.warps.PrivateWarp;
import com.youtube.hempfest.warps.PublicWarp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GUI {

	private static final Map<MenuType, UUID> util = new HashMap<>();
	private static final NamespacedKey key = new NamespacedKey(HempfestWarps.getInstance(), "hwarps_item");

	public static NamespacedKey getKey() {
		return key;
	}

	private static ItemStack getLeft() {
		ItemStack left = new ItemStack(Material.DARK_OAK_BUTTON);
		ItemMeta meta = left.getItemMeta();
		meta.setDisplayName(StringUtils.translate("&aLeft"));
		left.setItemMeta(meta);
		return left;
	}

	private static ItemStack getRight() {
		ItemStack left = new ItemStack(Material.DARK_OAK_BUTTON);
		ItemMeta meta = left.getItemMeta();
		meta.setDisplayName(StringUtils.translate("&aRight"));
		left.setItemMeta(meta);
		return left;
	}

	private static ItemStack getBack() {
		ItemStack left = new ItemStack(Material.BARRIER);
		ItemMeta meta = left.getItemMeta();
		meta.setDisplayName(StringUtils.translate("&cClose"));
		left.setItemMeta(meta);
		return left;
	}

	public static UUID getId(MenuType type) {
		return util.get(type);
	}

	public static PaginatedMenu select(MenuType type, UUID... uuid) {
		PaginatedMenu menu;
		List<UUID> ids = Arrays.asList(uuid);
		UUID id = null;
		LinkedList<String> append = new LinkedList<>();
		if (!ids.isEmpty()) {
			id = ids.get(0);
			append.addAll(PrivateWarp.ownedHomeNames(id));
			try {
				append.addAll(PrivateWarp.sharedHomeNames(id));
			} catch (NullPointerException ignored) {
			}
		}
		PaginatedBuilder builder = null;
		switch (type) {

			case HOMES:
				assert id != null;
				builder = new PaginatedBuilder(HempfestWarps.getInstance())
						.setTitle(StringUtils.translate(String.format(HempfestWarps.getGuiString("private-list-title"), (PrivateWarp.ownedHomeNames(id).size()), PrivateWarp.maxWarps(Bukkit.getPlayer(id)))))
						.setAlreadyFirst(HempfestWarps.getPrefix() + StringUtils.translate(" &cYou are on the first page of warps"))
						.setAlreadyLast(HempfestWarps.getPrefix() + StringUtils.translate(" &cYou don't have any more warps to view."))
						.setNavigationLeft(getLeft(), 48, PaginatedClick::sync)
						.setNavigationRight(getRight(), 50, PaginatedClick::sync)
						.setNavigationBack(getBack(), 49, click -> click.getPlayer().closeInventory())
						.setCloseAction(PaginatedClose::clear)
						.setupProcess(element -> element.applyLogic(e -> {
							if (GUI.getId(MenuType.HOMES).equals(e.getId())) {
								Material mat = Items.getMaterial(HempfestWarps.getGuiString("private-list-icon"));
								if (mat == null) {
									mat = Material.PAPER;
								}
								Material finalMat = mat;
								e.buildItem(() -> {
									ItemStack i = e.getItem();
									i.setType(finalMat);
									ItemMeta meta = i.getItemMeta();
									meta.setDisplayName(StringUtils.translate("&b&oGoto &f&o" + e.getContext()));
									meta.setLore(Arrays.asList("", StringUtils.translate("&7&oClick to warp.")));
									meta.getPersistentDataContainer().set(GUI.getKey(), PersistentDataType.STRING, e.getContext());
									i.setItemMeta(meta);
									return i;
								});
								e.action().setClick(click -> {
									String home = click.getClickedItem().getItemMeta().getPersistentDataContainer().get(GUI.getKey(), PersistentDataType.STRING);
									Bukkit.dispatchCommand(click.getPlayer(), "go " + home);
								});
							}
						}))
						.addBorder()
						.setBorderType(Material.GRAY_STAINED_GLASS_PANE)
						.setFillType(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
						.fill()
						.collect(new LinkedList<>(append))
						.limit(28);
				break;

			case HOMES_OTHER:
				assert id != null;
				UUID finalId = id;
				builder = new PaginatedBuilder(HempfestWarps.getInstance())
						.setTitle(StringUtils.translate(HempfestWarps.getGuiString("private-list-title-other").replace("{OWNER}", Objects.requireNonNull(Bukkit.getOfflinePlayer(id).getName()))))
						.setAlreadyFirst(HempfestWarps.getPrefix() + StringUtils.translate(" &cYou are on the first page of warps"))
						.setAlreadyLast(HempfestWarps.getPrefix() + StringUtils.translate(" &c" + Bukkit.getOfflinePlayer(id).getName() + " doesn't have any more warps to view."))
						.setNavigationLeft(getLeft(), 48, PaginatedClick::sync)
						.setNavigationRight(getRight(), 50, PaginatedClick::sync)
						.setNavigationBack(getBack(), 49, click -> click.getPlayer().closeInventory())
						.setCloseAction(PaginatedClose::clear)
						.setupProcess(element -> element.applyLogic(e -> {
							if (GUI.getId(MenuType.HOMES_OTHER).equals(e.getId())) {
								Material mat = Items.getMaterial(HempfestWarps.getGuiString("private-list-icon"));
								if (mat == null) {
									mat = Material.PAPER;
								}
								Material finalMat = mat;
								e.buildItem(() -> {
									ItemStack i = e.getItem();
									i.setType(finalMat);
									ItemMeta meta = i.getItemMeta();
									meta.setDisplayName(StringUtils.translate("&b&oGoto &f&o" + e.getContext()));
									meta.setLore(Arrays.asList("", StringUtils.translate("&7&oClick to warp.")));
									meta.getPersistentDataContainer().set(GUI.getKey(), PersistentDataType.STRING, e.getContext());
									i.setItemMeta(meta);
									return i;
								});
								e.action().setClick(click -> {
									String home = click.getClickedItem().getItemMeta().getPersistentDataContainer().get(GUI.getKey(), PersistentDataType.STRING);
									Bukkit.dispatchCommand(click.getPlayer(), "goto " + Bukkit.getOfflinePlayer(finalId).getName() + " " + home);
								});
							}
						}))
						.addBorder()
						.setBorderType(Material.GRAY_STAINED_GLASS_PANE)
						.setFillType(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
						.fill()
						.collect(new LinkedList<>(append))
						.limit(28);
				break;
			case WARPS:
				builder = new PaginatedBuilder(HempfestWarps.getInstance())
						.setTitle(StringUtils.translate(HempfestWarps.getGuiString("public-list-title")))
						.setAlreadyFirst(HempfestWarps.getPrefix() + StringUtils.translate(" &cYou are on the first page of warps"))
						.setAlreadyLast(HempfestWarps.getPrefix() + StringUtils.translate(" &cThere are no more public warps to view."))
						.setNavigationLeft(getLeft(), 48, PaginatedClick::sync)
						.setNavigationRight(getRight(), 50, PaginatedClick::sync)
						.setNavigationBack(getBack(), 49, click -> click.getPlayer().closeInventory())
						.setCloseAction(PaginatedClose::clear)
						.setupProcess(element -> element.applyLogic(e -> {
							if (GUI.getId(MenuType.WARPS).equals(e.getId())) {
								Material mat = Items.getMaterial(HempfestWarps.getGuiString("public-list-icon"));
								if (mat == null) {
									mat = Material.PAPER;
								}
								Material finalMat = mat;
								e.buildItem(() -> {
									ItemStack i = e.getItem();
									i.setType(finalMat);
									ItemMeta meta = i.getItemMeta();
									meta.setDisplayName(StringUtils.translate("&b&oGoto &f&o" + e.getContext()));
									meta.setLore(Arrays.asList("", StringUtils.translate("&7&oClick to warp.")));
									meta.getPersistentDataContainer().set(GUI.getKey(), PersistentDataType.STRING, e.getContext());
									i.setItemMeta(meta);
									return i;
								});
								e.action().setClick(click -> {
									String warp = click.getClickedItem().getItemMeta().getPersistentDataContainer().get(GUI.getKey(), PersistentDataType.STRING);
									Bukkit.dispatchCommand(click.getPlayer(), "warp " + warp);
								});
							}
						}))
						.addBorder()
						.setBorderType(Material.GRAY_STAINED_GLASS_PANE)
						.setFillType(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
						.fill()
						.collect(new LinkedList<>(PublicWarp.allWarps()))
						.limit(28);
				break;
			default:
				throw new IllegalStateException("Unexpected menu type: " + type);
		}
		util.put(type, builder.getId());
		menu = builder.build();
		return menu;
	}

	public enum MenuType {
		HOMES, HOMES_OTHER, WARPS
	}

}
