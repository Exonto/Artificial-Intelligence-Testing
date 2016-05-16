package com.gmail.username.tylersyme.survivor.entities.ai;

import java.util.ArrayList;
import java.util.List;

import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.Tile;
import com.gmail.username.tylersyme.survivor.world.TileMap;

public class Path {
	
	public List<Node> nodePath = new ArrayList<Node>();
	public List<Location> gridPath = new ArrayList<Location>();
	public List<Tile> tilePath = new ArrayList<Tile>();
	private boolean isCalculated = false; //If this path has been found (Used to determine if the pathfinder queue has processed this path)
	
	public int pathProgressIndex = 0; //This is zero indexed
	public Location currentPosition; //Represents the current position in the path - This will be null if tilePath does not contain this GridPosition
	
	private TileMap map;
	private Location origin;
	private Location goal;
	private boolean allowsDiagonals;
	public Path(List<Node> nodePath) {
		this.setNodePath(nodePath);
	}
	public Path(TileMap tileMap, Location origin, Location goal, boolean allowDiagonals) {
		this.setMap(tileMap);
		this.setOrigin(origin);
		this.setGoal(goal);
		this.setAllowsDiagonals(allowDiagonals);
	}
	
	public void updateProgess(Location currentPos) {
		if (this.getCurrentNode().pos.sameAs(currentPos)) return; //If nothing has changed
		
		if (this.getNextNode().pos.sameAs(currentPos)) {
			this.pathProgressIndex += 1;
		} else {
			Integer pathIndex = this.getPathIndex(currentPos);
			if (pathIndex != null) {
				this.pathProgressIndex = pathIndex;
				this.currentPosition = this.gridPath.get(this.pathProgressIndex);
			} else {
				this.pathProgressIndex = 0;
				this.currentPosition = null; //The currentPos is no longer in the path
			}
		}
	}
	
	/**
	 * Sets this path's node path.
	 * @param nodePath
	 */
	public void setNodePath(List<Node> nodePath) {
		this.nodePath = nodePath;
		for (int xI = 0; xI < nodePath.size(); xI++) {
			Node node = nodePath.get(xI);
			gridPath.add(node.pos);
			tilePath.add(node.tileMap.getTile(node.pos));
		}
		
		this.setCalculated(true);
	}
	
	public boolean pathExists() {
		return this.gridPath.size() > 0;
	}
	
	/**
	 * Returns the index location of the GridPosition in the tilePath list<p>
	 * Will return null if the tile path does not contain the GridPosition
	 * @param pos
	 * @return
	 */
	public Integer getPathIndex(Location pos) {
		for (int xI = 0; xI < gridPath.size(); xI++) {
			Location tilePos = gridPath.get(xI);
			if (tilePos.sameAs(pos)) {
				return xI;
			}
		}
		return null;
	}
	
	/**
	 * Returns if this path remains unblocked.
	 * @return
	 */
	public boolean isValid(int currentProgress) {
		for (int xI = currentProgress; xI < this.gridPath.size(); xI++) { //Skips the current location and evaluates all other nodes
			Location loc = this.gridPath.get(xI);
			if (loc.getTile().isPassable() == false) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns if the tile directly in front of the entity's path is blocked.
	 * @return
	 */
	public boolean canContinue(int currentProgress) {
		if (currentProgress + 1 < this.gridPath.size() && this.gridPath.get(currentProgress + 1).getTile().isPassable() == false) {
			return false;
		}
		
		return true;
	}
	
	public Node getCurrentNode() {
		return this.nodePath.get(pathProgressIndex);
	}
	
	public Node getNextNode() {
		Node nextNode = (this.hasNextNode()) 
				? (this.nodePath.get(pathProgressIndex + 1)) : (null);
		return nextNode;
	}
	
	public Node getPreviousNode() {
		Node nextNode = (this.hasPreviousNode()) 
				? (this.nodePath.get(pathProgressIndex - 1)) : (null);
		return nextNode;
	}
	
	public Node getStartNode() {
		return this.nodePath.get(0);
	}
	
	public Node getEndNode() {
		return this.nodePath.get(this.nodePath.size() - 1);
	}
	
	public boolean hasNextNode() {
		return (this.pathProgressIndex + 1 <= this.nodePath.size() - 1);
	}
	
	public boolean hasPreviousNode() {
		return (this.pathProgressIndex - 1 >= 0);
	}
	
	public boolean pathContains(Location pos) {
		for (int xI = 0; xI < gridPath.size(); xI++) {
			Location tilePos = gridPath.get(xI);
			if (tilePos.sameAs(pos)) {
				return true;
			}
		}
		return false;
	}

	public boolean isCalculated() {
		return isCalculated;
	}

	public void setCalculated(boolean isCalculated) {
		this.isCalculated = isCalculated;
	}
	public TileMap getMap() {
		return map;
	}
	public void setMap(TileMap map) {
		this.map = map;
	}
	public Location getOrigin() {
		return origin;
	}
	public void setOrigin(Location origin) {
		this.origin = origin;
	}
	public Location getGoal() {
		return goal;
	}
	public void setGoal(Location goal) {
		this.goal = goal;
	}
	public boolean allowsDiagonals() {
		return allowsDiagonals;
	}
	public void setAllowsDiagonals(boolean allowsDiagonals) {
		this.allowsDiagonals = allowsDiagonals;
	}
	
}
