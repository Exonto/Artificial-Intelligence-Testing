package com.gmail.username.tylersyme.survivor.world;

import com.gmail.username.tylersyme.survivor.world.tiles.AirTile;
import com.gmail.username.tylersyme.survivor.world.tiles.DebugBarrierTile;
import com.gmail.username.tylersyme.survivor.world.tiles.GrassTile;
import com.gmail.username.tylersyme.survivor.world.tiles.PineTreeTile;

/**
 * Each tile will have its own unique tile type. This tile type, because it's unique,
 * can be used to get a new instance of a tile. 
 * <p>
 * No tile will share the same TileType.
 */
public enum TileType {

	AIR(new AirTile(), false),
	GRASS(new GrassTile(), false),
	DEBUG_BARRIER(new DebugBarrierTile(), false),
	PINE_TREE(new PineTreeTile(), true);
	
	private Tile templateTile;
	private boolean isTerrain;
	TileType(Tile templateTile, boolean isTerrain) {
		this.setTemplateTile(templateTile);
		this.setTerrain(isTerrain);
	}
	
	/**
	 * Returns a new instance of the tile of this type
	 * @return
	 */
	public Tile getNewTile() {
		Tile newTile = (Tile) this.getTemplateTile().clone();
		newTile.setType(this);
		return newTile;
	}
	
	public Tile getTemplateTile() {
		return templateTile;
	}
	public void setTemplateTile(Tile templateTile) {
		this.templateTile = templateTile;
	}

	public boolean isTerrain() {
		return isTerrain;
	}
	public void setTerrain(boolean isTerrain) {
		this.isTerrain = isTerrain;
	}
	
}
























