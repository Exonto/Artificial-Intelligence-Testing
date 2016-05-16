package com.gmail.username.tylersyme.survivor.world;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.gmail.username.tylersyme.survivor.world.tiles.AirTile;

/**
 * Stores a Two Dimensional array of Tile objects that make up a world.
 */
public class TileMap {
	
	List<List<Tile>> map = new ArrayList<List<Tile>>();
	
	private World world;
	public TileMap(World world) {
		this.setWorld(world);
		
		this.loadWorld(); //Loads the map with empty tiles according the size of the world
	}
	
	/**
	 * Loads the map with empty tiles in preparation for real tiles to be added.
	 * <p>
	 * TileMap must load its world before any tiles can be set.
	 * @param size
	 */
	public void loadWorld(Dimension size) {
		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {
				map.get(x).set(y, new AirTile()); //Fill the space with air to possibly be replaced later
			}
		}
	}
	public void loadWorld() {
		Dimension size = this.getWorld().getSize();
		for (int x = 0; x < size.width; x++) {
			map.add(new ArrayList<Tile>());
			for (int y = 0; y < size.height; y++) {
				AirTile air = new AirTile();
				air.setLocation(new Location(this.getWorld(), x, y));
				map.get(x).add(air); //Fill the space with air to possibly be replaced later
			}
		}
	}
	
	/**
	 * Loads a flat world of grass with the dimensions of the World size.
	 */
	public void loadFlatWorld(TileType type) {
		int width = this.getWorld().getSize().width;
		int height = this.getWorld().getSize().height;
		
		this.fill(new Location(this.getWorld(), 0, 0), new Location(this.getWorld(), width - 1, height - 1), type); //-1 accounts for array indexing (width and height are not zero indexed)
	}
	
	/**
	 * Sets the given location to a new tile.
	 * <p>
	 * The map must be loaded beforehand.
	 * @param location
	 * @param tile
	 */
	public void setTile(Location location, TileType type) {
		if (this.isOutOfBounds(location)) {
			try {
				throw new Exception("Error: A corner was out of bounds");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (type.isTerrain()) { //If the tile is a terrain tile
			TerrainTile terrainTile = (TerrainTile) type.getNewTile();
			Tile groundTile = this.getWorld().getTile(location); //Retrieves the tiles at this location in the world map
			
			terrainTile.setGroundTile(groundTile);
			
			groundTile.setTerrainTile(terrainTile);
			groundTile.setPassable(terrainTile.isPassable());
			
			terrainTile.setLocation(location);
			this.map.get(location.getX()).set(location.getY(), terrainTile);
		} else { //If the tile is a regular tile
			Tile tile = type.getNewTile();
			
			tile.setLocation(location);
			this.map.get(location.getX()).set(location.getY(), tile);
		}
	}
	
	/**
	 * Returns the tile at the given location within the world.
	 * @param location
	 * @return
	 */
	public Tile getTile(Location location) {
		if (this.isOutOfBounds(location)) {
			return null;
		}
		int xPos = location.getX();
		int yPos = location.getY();
		
		return this.map.get(xPos).get(yPos);
	}
	
	/**
	 * Returns the tiles that make up the outer edges of a circle at a given point.
	 * Will not include tiles outside the bounds of the world.
	 * @param origin
	 * @param radius
	 * @return
	 */
	public List<Tile> getHollowCircle(Location origin, int radius) {
		List<Tile> circle = new ArrayList<Tile>();

		for (int xI = 0; xI < 360; xI++) {
			int degrees = xI;
			Double xValue = (double) Math.round(radius * Math.cos(degrees));
			Double yValue = (double) Math.round(radius * Math.sin(degrees));
			
			Location location = new Location(this.getWorld(), xValue.intValue() + origin.getX(), yValue.intValue() + origin.getY());
			if (this.isOutOfBounds(location) == false) {
				Tile tile = this.getTile(location);
				if (tile != null && circle.contains(tile) == false) {
					circle.add(tile);
				}
			}
		}
		
		return circle;
	}
	
	public List<Tile> getHollowCircleOptimized(Location origin, int radius) {
		List<Tile> circle = new ArrayList<Tile>();
		
		int xPos = radius, yPos = 0;
		int distanceSquared = 0;
		
		if (radius <= 0) return null;
		
		Location loc;
		loc = new Location(world, origin.getX() - radius, origin.getY());
		circle.add(this.getTile(loc));
		loc = new Location(world, origin.getX() + radius, origin.getY());
		circle.add(this.getTile(loc));
		loc = new Location(world, origin.getX(), origin.getY() - radius);
		circle.add(this.getTile(loc));
		loc = new Location(world, origin.getX(), origin.getY() + radius);
		circle.add(this.getTile(loc));
		
		while (xPos > yPos) {
			distanceSquared -= (--xPos) - (++yPos);
			if (distanceSquared < 0) distanceSquared += xPos++;
			
			loc = new Location(world, origin.getX() - xPos, origin.getY() - yPos);
			circle.add(this.getTile(loc));
			loc = new Location(world, origin.getX() - yPos, origin.getY() - xPos);
			circle.add(this.getTile(loc));
			loc = new Location(world, origin.getX() + yPos, origin.getY() - xPos);
			circle.add(this.getTile(loc));
			loc = new Location(world, origin.getX() + xPos, origin.getY() - yPos);
			circle.add(this.getTile(loc));
			loc = new Location(world, origin.getX() - xPos, origin.getY() + yPos);
			circle.add(this.getTile(loc));
			loc = new Location(world, origin.getX() - yPos, origin.getY() + xPos);
			circle.add(this.getTile(loc));
			loc = new Location(world, origin.getX() + yPos, origin.getY() + xPos);
			circle.add(this.getTile(loc));
			loc = new Location(world, origin.getX() + xPos, origin.getY() + yPos);
			circle.add(this.getTile(loc));
		}
		
		return circle;
	}
	
	/**
	 * Returns a filled circle of tiles.
	 * @param origin
	 * @param radius
	 * @return
	 */
	public List<Tile> getConvexCircle(Location origin, int radius) {
		List<Tile> convexCircle = new ArrayList<Tile>();
		
		for (int xI = 0; xI < radius; xI++) {
			List<Tile> circle = world.worldMap.getHollowCircle(origin, xI);
			
			convexCircle.addAll(circle);
		}
		return convexCircle;
	}
	
	public List<Tile> getConvexCircleOptimized(Location origin, int radius) {
		List<Tile> convexCircle = new ArrayList<Tile>();
		
		for (int xI = 0; xI < radius; xI++) {
			List<Tile> circle = world.worldMap.getHollowCircleOptimized(origin, xI);
			if (circle != null) {
				convexCircle.addAll(circle);
			}
		}
		return convexCircle;
	}
	
	/**
	 * Fills a region within the two points with the given Tile objects.
	 * @param firstCorner
	 * @param secondCorner
	 * @param tile
	 * @throws Exception 
	 */
	public void fill(Location firstCorner, Location secondCorner, TileType type) {
		if (this.isOutOfBounds(firstCorner) || this.isOutOfBounds(secondCorner)) { //If either corner is out of bounds
			try {
				throw new Exception("Error: A corner was out of bounds");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		int closestX = firstCorner.getX() < secondCorner.getX() ? (firstCorner.getX()) : (secondCorner.getX()); //Gets the corner with an X position closest to 0
		int farthestX = firstCorner.getX() > secondCorner.getX() ? (firstCorner.getX()) : (secondCorner.getX()); //Gets the corner with an X position farthest from 0
		
		int closestY = firstCorner.getY() < secondCorner.getY() ? (firstCorner.getY()) : (secondCorner.getY()); //Gets the corner with an Y position farthest from 0
		int farthestY = firstCorner.getY() > secondCorner.getY() ? (firstCorner.getY()) : (secondCorner.getY()); //Gets the corner with an Y position farthest from 0
		
		//{X = row and Y = column}
		//Will fill the area starting at the first row and working down the column
		for (int x = closestX; x < farthestX + 1; x++) {
			for (int y = closestY; y < farthestY + 1; y++) {
				Location location = new Location(this.getWorld(), x, y);

				this.setTile(location, type);
			}
		}
	}
	
	/**
	 * Returns if the location is outside the bounds of the map.
	 * @param location
	 * @return
	 */
	public boolean isOutOfBounds(Location location) {
		boolean outOfBounds = false;
		
		Dimension worldSize = this.getWorld().getSize();
		if (location.getX() < 0 || location.getX() >= worldSize.width) {
			outOfBounds = true;
		} else if (location.getY() < 0 || location.getY() >= worldSize.height) {
			outOfBounds = true;
		}
		
		return outOfBounds;
	}
	
	//Getters and Setters
	
	/**
	 * Returns the two dimensional map in a linear map.
	 * Used to allow easy looping through tiles.
	 * @return
	 */
	public List<Tile> getLinearMap() {
		List<Tile> linearMap = new ArrayList<Tile>();
		for (int x = 0; x < this.map.size(); x++) {
			linearMap.addAll(this.map.get(x));
		}
		
		return linearMap;
	}
	
	public World getWorld() {
		return world;
	}
	public void setWorld(World world) {
		this.world = world;
	}

}

























