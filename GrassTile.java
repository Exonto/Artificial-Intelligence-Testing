package com.gmail.username.tylersyme.survivor.world.tiles;

import com.gmail.username.tylersyme.survivor.ResourceLoader;
import com.gmail.username.tylersyme.survivor.world.Tile;
import com.gmail.username.tylersyme.survivor.world.TileType;

public class GrassTile extends Tile {

	public GrassTile() {
		super(TileType.GRASS, ResourceLoader.getLocalImage("/GrassTile.png"), true);
	}

}
