package com.youtube.hempfest.warps.structure;

import com.github.sanctum.labyrinth.library.HUID;
import java.io.IOException;
import org.bukkit.Location;

public interface Warp {

	public Location getLocation() throws IOException, ClassNotFoundException;

	public String getOwner();

	public HUID getId() throws IOException, ClassNotFoundException;

}
