package com.youtube.hempfest.warps.structure;

import com.youtube.hempfest.hempcore.library.HUID;
import java.io.IOException;
import org.bukkit.Location;

public interface Warp {

	public Location getLocation() throws IOException, ClassNotFoundException;

	public String getOwner() throws IOException, ClassNotFoundException;

	public HUID getId() throws IOException, ClassNotFoundException;

}
