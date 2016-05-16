package com.gmail.username.tylersyme.survivor.world.tiles;

import com.gmail.username.tylersyme.survivor.ResourceLoader;
import com.gmail.username.tylersyme.survivor.world.Tile;
import com.gmail.username.tylersyme.survivor.world.TileType;

public class DebugBarrierTile extends Tile {

	public DebugBarrierTile() {
		super(TileType.DEBUG_BARRIER, ResourceLoader.getLocalImage("/DebugBarrier.png"), false);
	}

}
