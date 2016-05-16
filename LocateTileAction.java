package com.gmail.username.tylersyme.survivor.entities.ai.actions;

import java.util.ArrayList;
import java.util.List;

import com.gmail.username.tylersyme.survivor.entities.Animal;
import com.gmail.username.tylersyme.survivor.entities.ai.Action;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.movementpatterns.MovementPattern;
import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.Tile;
import com.gmail.username.tylersyme.survivor.world.TileType;
import com.gmail.username.tylersyme.survivor.world.World;

public class LocateTileAction extends Action {

	private boolean located = false;
	private List<Tile> locatedTiles = new ArrayList<Tile>(); //Multiple tiles can be found
	
	private TileType goalTile;
	private MovementPattern movePattern; //The pattern in which this action will attempt to locate a tile
	public LocateTileAction(boolean isSuperAction, Animal animal, TileType type, MovementPattern movePattern) {
		super(isSuperAction, ActionType.LOCATE_TILE, animal);
		
		this.setGoalTile(type);
		this.setMovePattern(movePattern);
	}

	@Override
	public void action(double elapsed) {
		if (this.isLocated() == false) { //If the latest search produced no results
			this.addSubAction(this.getMovePattern().getNextMovement()); //Gives itself a child movement action to search for a tile
				
			return;
		}
	}
	
	@Override
	public void evaluate() {
		MoveAction movement = (MoveAction) this.getNextAction();
		if (movement != null && movement.isCancelled()) { //If this the entity could not find a path to the goal
			this.removeSubActions();
			MoveAction newRandomMovement = this.getMovePattern().getNextMovement();
			this.addSubAction(newRandomMovement); //Gives itself a child movement action to search for a tile
		}
		
		if (this.isGoalInSight()) {
			this.getNextAction().setCompleted(true); //Stop moving (the tile has been located)
			this.setCompleted(true);
		}
	}
	
	protected boolean isGoalInSight() {
		Animal animal = (Animal) this.getEntity();

		for (Tile tile : animal.getVisibleTiles()) {
			if (tile.getType() == this.getGoalTile() && this.isReachable(tile.getLocation())) { //Ground
				this.setLocated(true);
				this.getLocatedTiles().add(tile);
				
				return true;
			} else if (tile.hasTerrain() && tile.getTerrainTile().getType() == this.getGoalTile() && this.isReachable(tile.getLocation())) { //Terrain
				this.setLocated(true);
				this.getLocatedTiles().add(tile.getTerrainTile());
				
				return true;
			}
		}
		this.setLocated(false);
		
		return false;
	}
	
	/**
	 * Returns whether or not the food that has been found can be reached.
	 * @param possibleFoodLocation
	 * @return
	 */
	private boolean isReachable(Location possibleFoodLocation) {
		Animal animal = (Animal) this.getEntity();
		World world = possibleFoodLocation.getWorld();
		
		return possibleFoodLocation.getClosestOpenAdjacent(world, animal.getLocation()) != null;
	}
	

	public TileType getGoalTile() {
		return goalTile;
	}

	public void setGoalTile(TileType goalTile) {
		this.goalTile = goalTile;
	}

	public boolean isLocated() {
		return located;
	}

	public void setLocated(boolean located) {
		this.located = located;
	}

	public List<Tile> getLocatedTiles() {
		return locatedTiles;
	}

	public void setLocatedTiles(List<Tile> locatedTiles) {
		this.locatedTiles = locatedTiles;
	}

	public MovementPattern getMovePattern() {
		return movePattern;
	}

	public void setMovePattern(MovementPattern movePattern) {
		this.movePattern = movePattern;
	}

}
