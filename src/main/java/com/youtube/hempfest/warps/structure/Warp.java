package com.youtube.hempfest.warps.structure;

import com.youtube.hempfest.warps.system.WID;
import org.bukkit.Location;

public interface Warp {

	public Location getLocation();

	public String getOwner();

	public WID getId();

}
