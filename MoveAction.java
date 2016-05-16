package com.gmail.username.tylersyme.survivor.entities.ai.actions;

import com.gmail.username.tylersyme.survivor.Main;
import com.gmail.username.tylersyme.survivor.entities.Entity;
import com.gmail.username.tylersyme.survivor.entities.ai.Action;
import com.gmail.username.tylersyme.survivor.entities.ai.Path;
import com.gmail.username.tylersyme.survivor.entities.ai.Pathfinder;
import com.gmail.username.tylersyme.survivor.world.Location;

public class MoveAction extends Action {

	private Path pathToGoal;
	private boolean isCurrentPathValid = true;
	
	private Location goal;
	private double speed; //Tiles Per Second
	private double progressToAdjacentTile = 0.0; //When this equals 1000.0, move to the adjacent tile
	private int pathProgressIndex = 1; //Set to 1 to skip the tile that the entity beings at
	public MoveAction(boolean isSuperAction, Entity entity, Location goal, double speed) {
		super(isSuperAction, ActionType.MOVE, entity);
		
		this.setGoal(goal);
		this.setSpeed(speed);

		pathToGoal = Pathfinder.requestPathBetween(Main.gameplayScene.getCurrentWorld().worldMap, this.getEntity().getLocation(), this.getGoal(), true);
	}
	public MoveAction(boolean isSuperAction, Entity entity, Location origin, Location goal, double speed) { //Allows a movement to be executed at a custom origin
		super(isSuperAction, ActionType.MOVE, entity);
		
		this.setGoal(goal);
		this.setSpeed(speed);
		
		pathToGoal = Pathfinder.requestPathBetween(Main.gameplayScene.getCurrentWorld().worldMap, origin, this.getGoal(), true);
	}

	@Override
	public void action(double elapsed) {
		if (pathToGoal.isCalculated() == false) { //If the path request has not yet been fulfilled
			return; //Wait for the next frame
		}
		
		if (pathToGoal == null || pathToGoal.pathExists() == false) { //If no path exists between the entity and the goal
			this.cancelAction();
			return;
		} else if (pathToGoal.getStartNode().pos.sameAs(pathToGoal.getEndNode().pos)) { //If the entity start location and goal location are the same
			this.setCompleted(true);
			return;
		}
		
		
		
		if (this.progressToAdjacentTile >= 1000.0) {
			this.progressToAdjacentTile -= 1000.0;
			
			if (this.pathToGoal.isValid(this.pathProgressIndex) == false) { //Re-evaluate the current path
				this.setCurrentPathValid(false);
				this.reevaluatePath();
				
				if (pathToGoal == null || pathToGoal.pathExists()) { //If no path exists between the entity and the goal
					this.cancelAction();
					return;
				}
			}
			this.setCurrentPathValid(true);
			
			Location nextLocation = pathToGoal.gridPath.get(this.pathProgressIndex);
			this.getEntity().setLocation(nextLocation);
			
			this.pathProgressIndex += 1; //Advance ahead to the next location in the path
			
			if (this.pathProgressIndex >= this.pathToGoal.gridPath.size()) { //If the end of the path has been reached
				this.setCompleted(true);
			}
		}
		this.progressToAdjacentTile += speed * elapsed; //Progress forward
	}
	
	@Override
	public void evaluate() { /*Unused*/ }
	
	/**
	 * Will determine a new path to the origin location.
	 */
	private void reevaluatePath() {
		pathToGoal = Pathfinder.getPathBetween(Main.gameplayScene.getCurrentWorld().worldMap, this.getEntity().getLocation(), this.getGoal(), true);
		this.pathProgressIndex = 1; //Reset progress
	}
	
	
	public Location getGoal() {
		return goal;
	}

	public void setGoal(Location goal) {
		this.goal = goal;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Path getPathToGoal() {
		return pathToGoal;
	}
	
	public int getPathProgress() {
		return this.pathProgressIndex;
	}
	
	public boolean isCurrentPathValid() {
		return isCurrentPathValid;
	}
	
	private void setCurrentPathValid(boolean isCurrentPathValid) {
		this.isCurrentPathValid = isCurrentPathValid;
	}

}
