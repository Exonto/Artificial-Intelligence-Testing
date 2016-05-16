package com.gmail.username.tylersyme.survivor.world;

import java.awt.image.BufferedImage;

/**
 * Represents a tile that will sit atop a regular tile in the world and will be rendered after the world's regular tiles.
 * <p>
 * This tile sub type is used to add various terrain objects such as trees and boulders into the world.
 */
public abstract class TerrainTile extends Tile {

	private Tile groundTile;
	public TerrainTile(TileType type, Tile groundTile, BufferedImage image, boolean isPassable) {
		super(type, image, isPassable);
		
		this.setGroundTile(groundTile);
		this.getGroundTile().setTerrainTile(this); //Set the ground tile's terrain tile to this Object
		this.getGroundTile().setPassable(this.isPassable()); //Changes the passability of its ground tile
	}
	public TerrainTile(TileType type, BufferedImage image, boolean isPassable) { //Used for static objects that have no ground tile
		super(type, image, isPassable);
	}
	
	
	public Tile getGroundTile() {
		return groundTile;
	}
	public void setGroundTile(Tile groundTile) {
		this.groundTile = groundTile;
	}

}
