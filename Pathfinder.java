package com.gmail.username.tylersyme.survivor.entities.ai;

import java.util.ArrayList;
import java.util.List;

import com.gmail.username.tylersyme.survivor.Main;
import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.Tile;
import com.gmail.username.tylersyme.survivor.world.TileMap;
import com.gmail.username.tylersyme.survivor.world.World;

public class Pathfinder {
	
	public static List<Path> pathRequestQueue = new ArrayList<Path>();
	public static Path requestPathBetween(TileMap tileMap, Location origin, Location goal, boolean allowDiagonals) {
		Path request = new Path(tileMap, origin, goal, allowDiagonals); //The empty path
		
		pathRequestQueue.add(request);
		return request;
	}
	
	/**
	 * Will calculate the given number of paths, and set them to {@code located}.
	 * @param amount
	 */
	public static void processPathRequests(int amount) {
		for (int xI = 0; xI < amount; xI++) {
			if (xI > pathRequestQueue.size() - 1) {
				return;
			}
			
			Path request = pathRequestQueue.get(xI);
			Path path = getPathBetween(request.getMap(), request.getOrigin(), request.getGoal(), request.allowsDiagonals());
			if (path != null) { //If there is a path to the goal
				request.setNodePath(path.nodePath);
			} else {
				request.setCalculated(true); //The path has been calculated, its just equal to null
			}
			
			pathRequestQueue.remove(xI);
		}
	}
	
	public static void update(double elapsed) {
		
	}
	
	/**
	 * Returns a direct path between two points (including the origin).</p>
	 * This will return null if no path can be found.<p>
	 * It is proper to request for a path rather than call this method directly.
	 * 
	 * 
	 * @param origin
	 * @param goal
	 * @return Path
	 */
	public static Path getPathBetween(TileMap tileMap, Location origin, Location goal, boolean allowDiagonals) {
		World world = Main.gameplayScene.getCurrentWorld();
		
		Tile originTile = world.getTile(origin);
		Tile goalTile = world.getTile(goal);
		
		List<Node> path = new ArrayList<Node>();
		
		List<Node> valid = new ArrayList<Node>(); //List of nodes that have not been ruled out
		List<Node> invalid = new ArrayList<Node>(); //List of nodes that have been ruled out and will not be used in further calculations
		
		if (originTile == null || goalTile == null) return null;
		
		if (origin == goal || goalTile.isPassable() == false) return null;
		
		if (origin.isAdjacent(goal)) { //If the goal is directly adjacent to the origin
			path.add(new Node(tileMap, origin));
			path.add(new Node(tileMap, goal));
			Path newPath = new Path(path);
			newPath.setCalculated(true);
			
			return newPath;
		}
		
		boolean pathFound = false;
		Node start = new Node(tileMap, origin);
		Node end = new Node(tileMap, goal);
		Node current = start; //The node that is currently being examined (starting with the origin tile)
		while (!pathFound) {
			if (current == null) return null; //If no path exists
			pathFound = handleAdjacentNodes(current, end, valid, invalid, allowDiagonals); //Do all the mathy stuff to the adjacent nodes, returns if the path was found
			
			if (pathFound == true) {
				//Track up through the node parents to find the path
				Node pathNode = end;
				while (true) {
					path.add(0, pathNode); //Inserts the path into the first index because we're building the path backwards
					if (pathNode.parent == null) return new Path(path); //End of the path
					pathNode = pathNode.parent;
				}
			}
			
			current = getLowest(valid);
		}
		
		return null;
	}
	
	private static boolean handleAdjacentNodes(Node origin, Node goal, List<Node> valid, List<Node> invalid, boolean allowDiagonals) {
		List<Node> adjacent = origin.adjacentPassableNodes(allowDiagonals);
		
		//Move the origin node to the invalid list
		valid.remove(origin);
		invalid.add(origin);
		
		//Calculate all fValues of valid, adjacent nodes
		for (int xI = 0; xI < adjacent.size(); xI++) {
			Node relative = adjacent.get(xI);
			if (contains(invalid, relative)) { continue; }
			relative.getFValue(origin, goal);
			if (contains(valid, relative) == false) { //If this node is brand new
				valid.add(relative);
				relative.parent = origin; 
			} else if (relative.parent != null && (origin.getMovementCost(relative) < relative.parent.getMovementCost(relative))) { //If this node has already been added to the valid list
				relative.parent = origin; //Change the parent node of the adjacent node
				relative.getRecalculatedFValue(origin, goal); //Needs to re-evaluate it's fValue
			}
			
			if (relative.pos.isAdjacent(goal.pos)) { //If the position is next too the goal position
				goal.parent = relative; //Set the end node's parent to the current node
				return true; //You found the end!
			}
		}
		
		return false; //Didn't find the end, repeat the cycle with the next node
	}
	
	private static Node getLowest(List<Node> valid) {
		Node smallest = null;
		for (int xI = 0; xI < valid.size(); xI++) {
			Node node = valid.get(xI);
			if (smallest == null || (node.fValue < smallest.fValue)) {
				smallest = node;
			}
		}
		return smallest;
	}
	
	private static boolean contains(List<Node> list, Node node) {
		for (int xI = 0; xI < list.size(); xI++) {
			Node listNode = list.get(xI);
			if (listNode.pos.sameAs(node.pos)) {
				return true;
			}
		}
		return false;
	}

}








