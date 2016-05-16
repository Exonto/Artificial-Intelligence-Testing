package com.gmail.username.tylersyme.survivor.world;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is the abstract object used to define the base elements of a tile. 
 */
public abstract class Tile implements Cloneable {

	public static List<Tile> allTiles = new ArrayList<Tile>();
	public static HashMap<TileType, Tile> allTilesByType = new HashMap<TileType, Tile>();
	
	private boolean debug_hide = false;
	
	private Location location;
	private TileType type;
	private TerrainTile terrainTile;
	private BufferedImage image;
	private boolean isDefaultPassable = true; //The default
	private boolean isPassable = true; //The current
	public Tile(TileType type, BufferedImage image, boolean isPassable) {
		this.setType(type);
		this.setImage(image);
		this.setDefaultPassable(isPassable);
		this.setPassable(isPassable);
		
		allTiles.add(this);
		allTilesByType.put(type, this);
	}
	
	public Tile(Tile tile) {
		
	}
	
	/**
	 * Returns a new instance of the tile
	 */
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
	
	/**
	 * Returns a <b>static</b> template tile. Do not use in worlds.
	 * @param type
	 * @return
	 */
	public static Tile getTileByType(TileType type) {
		return allTilesByType.get(type);
	}
	
	public boolean hasTerrain() {
		return this.getTerrainTile() != null;
	}
	
	
	
	
	
	
	
	
	public TileType getType() {
		return type;
	}
	
	public void setType(TileType type) {
		this.type = type;
	}

	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public boolean isPassable() {
		return isPassable;
	}

	public void setPassable(boolean isPassable) {
		this.isPassable = isPassable;
	}

	public TerrainTile getTerrainTile() {
		return terrainTile;
	}

	public void setTerrainTile(TerrainTile terrainTile) {
		this.terrainTile = terrainTile;
	}

	public boolean isDebug_Hide() {
		return debug_hide;
	}

	public void setDebug_Hide(boolean debug_hide) {
		this.debug_hide = debug_hide;
	}

	public boolean isDefaultPassable() {
		return isDefaultPassable;
	}

	public void setDefaultPassable(boolean isDefaultPassable) {
		this.isDefaultPassable = isDefaultPassable;
	}
	
}
