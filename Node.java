package com.gmail.username.tylersyme.survivor.entities.ai;

import java.util.ArrayList;
import java.util.List;

import com.gmail.username.tylersyme.survivor.Main;
import com.gmail.username.tylersyme.survivor.world.Direction;
import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.TileMap;
import com.gmail.username.tylersyme.survivor.world.World;

public class Node {
	
	public TileMap tileMap;
	public Location pos;
	public Node parent;
	public Integer heuristic;
	public Integer movementCost;
	public Integer baseMovementCost;
	public Integer fValue;
	public boolean isPassable;
	
	public boolean isDiagonal = false; //If this node is diagonal to its parent
	public Node(TileMap tileMap, Location pos) {
		this.tileMap = tileMap;
		this.pos = pos;
	}
	
	public int getHeuristic(Node goal) {
		if (heuristic == null) {
			int xDif = Math.abs(this.pos.getX() - goal.pos.getX());
			int yDif = Math.abs(this.pos.getY() - goal.pos.getY());
			int heuristic = xDif + yDif; 
			
			this.heuristic = heuristic * 10;
		} 
		return heuristic;
	}
	
	public int getMovementCost(Node origin) {
		if (movementCost == null) {
			int originMovementCost = (origin.movementCost == null) ? (0) : (origin.movementCost);
			if (this.pos.isOrthogonal(origin.pos)) {
				this.isDiagonal = false;
				
				this.baseMovementCost = 10;
			} else {
				this.isDiagonal = true;
				
				this.baseMovementCost = 14;
			}
			this.movementCost = baseMovementCost + originMovementCost;
		}
		return this.movementCost;
	}
	
	public int getFValue(Node origin, Node goal) {
		if (fValue == null) {
			this.fValue = this.getMovementCost(origin) + this.getHeuristic(goal);
		}
		return fValue;
	}
	
	/**
	 * Resets this node's movement cost and then recalculates the F Value
	 * @param origin
	 * @param goal
	 * @return
	 */
	public int getRecalculatedFValue(Node origin, Node goal) {
		this.movementCost = null;
		this.getMovementCost(origin);
		return this.getFValue(origin, goal);
	}
	
	public List<Node> adjacentNodes() {
		List<Node> adjacent = new ArrayList<Node>();
		
		for (int xI = 0; xI < Direction.values().length; xI++) {
			Direction dir = Direction.values()[xI];

			Location relative = this.pos.getRelative(dir, 1);
			
			if (Main.gameplayScene.getCurrentWorld().worldMap.isOutOfBounds(relative) == false) {
				adjacent.add(new Node(tileMap, relative));
			}
		}

		return adjacent;
	}
	
	/**
	 * Only returns adjacent tiles that are passable
	 * @return
	 */
	public List<Node> adjacentPassableNodes(boolean allowDiagonals) {
		List<Node> adjacent = new ArrayList<Node>();
		
		Direction[] directions = ((allowDiagonals) ? (Direction.values()) : (Direction.getNonDiagonals()));
		for (int xI = 0; xI < directions.length; xI++) {
			Direction dir = directions[xI];

			Location relative = this.pos.getRelative(dir, 1);
			
			World world = Main.gameplayScene.getCurrentWorld();
			
			if (world.worldMap.isOutOfBounds(relative) == false) { //This allows a path to built across non-existent tiles
				if ((world.getTile(relative) == null || world.getTile(relative).isPassable())) {
					adjacent.add(new Node(tileMap, relative));
				}
			}
		}

		return adjacent;
	}

}
