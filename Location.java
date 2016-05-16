package com.gmail.username.tylersyme.survivor.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gmail.username.tylersyme.survivor.Main;
import com.gmail.username.tylersyme.survivor.Settings;

/**
 * Specifically is used to represent the location of tiles in grid form.
 */
public class Location {
	
	private Point point;
	private World world;
	private int x;
	private int y;
	public Location(Point point) {
		this.setPoint(point);
		this.setX(x);
		this.setY(y);
	}
	public Location(int x, int y) {
		this.setPoint(new Point(x, y));
		this.setX(x);
		this.setY(y);
	}
	public Location(World world, int x, int y) {
		this.setWorld(world);
		this.setPoint(new Point(x, y));
		this.setX(x);
		this.setY(y);
	}
	
	public Tile getTile() {
		return this.getWorld().getTile(this);
	}
	public Tile getTerrainTile() {
		return this.getWorld().getTerrainTile(this);
	}
	
	/**
	 * Returns a random Location between the given ranges (min value is inclusive, max is exclusive)
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @return
	 */
	public static Location getRandomLocation(int xMin, int xMax, int yMin, int yMax) {
		if (xMin < 0 || yMin < 0) return null;
		
		Random r = new Random();
		
		int x = r.nextInt(xMax - xMin) + xMin;
		int y = r.nextInt(yMax - yMin) + yMin;
		
		return new Location(x, y);
	}
	public Location getRandomLocation(World world, int radius) {
		if (radius < 0) return null;
		
		Random r = new Random();
		
		int xMin,xMax,yMin,yMax;
		
		xMin = this.getX() - radius;
		xMax = this.getX() + radius;
		
		yMin = this.getY() - radius;
		yMax = this.getY() + radius;
		
		if (xMin < 0) {
			xMin = 0;
		}
		if (xMax > world.getSize().getWidth() - 1) {
			xMax = (int) world.getSize().getWidth();
		}
		if (yMin < 0) {
			yMin = 0;
		}
		if (yMax > world.getSize().getHeight() - 1) {
			yMax = (int) world.getSize().getHeight();
		}
		
		int x = r.nextInt(xMax - xMin) + xMin;
		int y = r.nextInt(yMax - yMin) + yMin;
		
		return new Location(this.getWorld(), x, y);
	} 
	
	/**
	 * Returns the raw pixel location of the location.
	 * @param includeCamera
	 * @return
	 */
	public Point getRawScreenPoint(boolean includeCamera) {
		int tileSize = Settings.defaultTileSize;
		
		World world = Main.gameplayScene.getCurrentWorld();
		Point raw; 
		if (includeCamera) {
			raw = new Point((int) ((this.x * tileSize) + world.getCamera().getX()), (int) ((this.y * tileSize) + world.getCamera().getY()));
		} else {
			raw = new Point(this.x * tileSize, this.y * tileSize);
		}
		
		return raw;
	}
	
	public Location add(int x, int y) {
		return new Location(this.getWorld(), this.x + x, this.y + y);
	}
	
	public boolean isAdjacent(Location pos) {
		List<Location> adjacent = this.adjacentLocations();
		for (int xI = 0; xI < adjacent.size(); xI++) {
			Location relative = adjacent.get(xI);
			if (relative.sameAs(pos)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Location> adjacentLocations() {
		List<Location> adjacent = new ArrayList<Location>();
		
		for (int xI = 0; xI < Direction.values().length; xI++) {
			Direction dir = Direction.values()[xI];
			Location relative = this.getRelative(dir, 1);
			
			if (Main.gameplayScene.getCurrentWorld().worldMap.isOutOfBounds(relative) == false) {
				adjacent.add(relative);
			}
		}

		return adjacent;
	}
	
	/**
	 * Compares this location and the given location to determine which adjacent location is facing the given the location.
	 * @param location
	 * @return
	 */
	public Location getClosestAdjacent(Location location) {
		Direction direction = Direction.getRelationship(this, location); //The angle at which this location is facing the given location
		
		return direction.getRelative(this);
	}
	
	public Location getClosestOpenAdjacent(World world, Location location) {
		Location closestAdjacentLocation = null;
		for (Direction dir : Direction.getNonDiagonals()) {
			
			Location adjacent = dir.getRelative(this);
			Tile tile = world.getTile(adjacent);
			if (tile != null && tile.isPassable()) { //If the tile exists and can be passed through
				if (closestAdjacentLocation == null) {
					closestAdjacentLocation = adjacent;
				} else if (location.getDistance(closestAdjacentLocation) > location.getDistance(adjacent)) { //If the next adjacent location is closer than the previous
					closestAdjacentLocation = adjacent;
				}
			}
		}
		return closestAdjacentLocation;
	}
	
	public Location getRelative(Direction dir, int amount) {
		return dir.getRelative(this, amount);
	}
	
	/**
	 * Gets the distance between this location and the given location.
	 * @param goal
	 * @return
	 */
	public int getDistance(Location goal) {
		int xDif = Math.abs(this.getX() - goal.getX());
		int yDif = Math.abs(this.getY() - goal.getY());
		
		return (int) Math.hypot(xDif, yDif);
	}
	
	/**
	 * Returns if this location can be reached (aka it is not blocked off).
	 * @return
	 */
	public boolean isReachable(World world) {
		for (Direction dir : Direction.values()) {
			Tile tile = world.worldMap.getTile(dir.getRelative(this));
			if (tile != null) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isOrthogonal(Location pos) {
		return (this.getX() == pos.getX() || this.getY() == pos.getY());
	}
	
	public boolean sameAs(Location pos) {
		return (this.x == pos.x && this.y == pos.y);
	}
	
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	
	@Override
	public String toString() {
		return this.getX() + ", " + this.getY();
	}
	public World getWorld() {
		return world;
	}
	public void setWorld(World world) {
		this.world = world;
	}
}
