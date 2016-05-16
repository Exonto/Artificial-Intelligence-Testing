package com.gmail.username.tylersyme.survivor.world.tiles;

import com.gmail.username.tylersyme.survivor.ResourceLoader;
import com.gmail.username.tylersyme.survivor.world.TerrainTile;
import com.gmail.username.tylersyme.survivor.world.Tile;
import com.gmail.username.tylersyme.survivor.world.TileType;

public class PineTreeTile extends TerrainTile {

	public PineTreeTile(Tile groundTile) {
		super(TileType.PINE_TREE, groundTile, ResourceLoader.getLocalImage("/PineTree.png"), false);
		
	}
	public PineTreeTile() {
		super(TileType.PINE_TREE, ResourceLoader.getLocalImage("/PineTree.png"), false);
		
	}

}
