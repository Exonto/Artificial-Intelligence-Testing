package com.gmail.username.tylersyme.survivor.world;


public enum Direction {
	
	EAST(1, 0),
	WEST(-1, 0),
	NORTH(0, 1),
	SOUTH(0, -1),
	N_EAST(1, 1),
	S_EAST(1, -1),
	N_WEST(-1, 1),
	S_WEST(-1, -1);
	
	public int relativeX;
	public int relativeY;
	private Direction(int relativeX, int relativeY) {
		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}
	
	/**
	 * Returns the direction relationship between two *adjacent* tiles
	 * @param pos1
	 * @param pos2
	 * @return
	 */
	public static Direction getDirection(Location loc1, Location loc2) {
		int difX = loc2.getX() - loc1.getX();
		int difY = loc2.getY() - loc1.getY();
		
		for (int xI = 0; xI < Direction.values().length; xI++) {
			Direction dir = Direction.values()[xI];
			
			if (dir.relativeX == difX && dir.relativeY == difY) {
				return dir;
			}
		}
		return null;
	}
	
	public static Direction getRelationship(Location origin, Location goal) {
		int x1 = origin.getX();
		int y1 = origin.getY();
		
		int x2 = goal.getX();
		int y2 = goal.getY();
		
		int xDif = x1 - x2;
		int yDif = x2 - y2;
		
		if (xDif > yDif) {
			if (x1 > x2) { //If the goal is to the west of the origin
				return Direction.WEST;
			} else { 
				return Direction.EAST;
			}
		} else {
			if (y1 > y2) { //If the goal is to the south of the origin
				return Direction.SOUTH;
			} else { 
				return Direction.NORTH;
			}
		}
		
	}
	
	public static Direction[] getNonDiagonals() {
		return new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };
	}
	
	public Location getRelative(Location origin) {
		return origin.add(this.relativeX, this.relativeY);
	}
	
	public Location getRelative(Location origin, int amount) {
		return origin.add(this.relativeX * amount, this.relativeY * amount);
	}
	
	public boolean isDiagonal() {
		return (this == N_EAST || this == S_EAST || this == N_WEST || this == S_WEST);
	}
	
}





















