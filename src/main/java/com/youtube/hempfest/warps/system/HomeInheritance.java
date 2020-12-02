package com.youtube.hempfest.warps.system;

import com.youtube.hempfest.hempcore.library.HFEncoded;
import com.youtube.hempfest.hempcore.library.HUID;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;

public class HomeInheritance implements Serializable {

	private List<UUID> users = new ArrayList<>();

	private HUID warpId;

	public HomeInheritance(HUID warpId) {
		this.warpId = warpId;
	}

	public void add(UUID uuid) {
		users.add(uuid);
	}

	public void remove(UUID uuid) {
		users.remove(uuid);
	}

	public List<String> getUsers() {
		List<String> array = new ArrayList<>();
		for (UUID id : users) {
			array.add(Bukkit.getOfflinePlayer(id).getName());
		}
		return array;
	}

	private void input(List<UUID> uuids) {
		this.users.addAll(uuids);
	}

	public void commit(Config file) throws IOException {
		HomeInheritance inh = new HomeInheritance(warpId);
		inh.input(users);
		file.getConfig().set("Shared." + warpId.toString(), new HFEncoded(inh).serialize());
		file.saveConfig();
	}

}
