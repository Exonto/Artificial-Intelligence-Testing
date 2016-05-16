package com.gmail.username.tylersyme.survivor.entities.ai.actions.movementpatterns;

import java.util.ArrayList;
import java.util.List;

import com.gmail.username.tylersyme.survivor.entities.Animal;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.MoveAction;
import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.World;

public class RandomSteppedMovementPattern extends RandomMovementPattern {

	private int stepDistance; //The number of tiles moved per step
	private int progress = 0;
	private List<MoveAction> steppedMovements = new ArrayList<MoveAction>();
	private MoveAction movement;
	public RandomSteppedMovementPattern(World world, Animal animal, int radius, int stepDistance) {
		super(world, animal, radius);
		
		this.setStepDistance(stepDistance);
	}
	
	@Override
	public MoveAction getNextMovement() {
		if (this.steppedMovements.size() == 0) {
			this.progress = 0;
			this.movement = super.getNextMovement(); //Get the full move action
			
			int pathSize = this.movement.getPathToGoal().gridPath.size();
			List<Location> gridPath = this.movement.getPathToGoal().gridPath;
			for (int xI = 0; xI < pathSize - 1; xI += this.getStepDistance()) {
				if (xI > pathSize - 1) {
					xI = pathSize - 1;
				}
				
				MoveAction step = new MoveAction(true, this.getAnimal(), gridPath.get(xI), gridPath.get(xI + this.getStepDistance()), this.getAnimal().getMovementSpeed());
				this.steppedMovements.add(step);
			}
		}
		
		if (this.progress > this.steppedMovements.size() - 1) { //If all of the movements have been fulfilled
			this.steppedMovements.clear(); //Resets this pattern
			return this.getNextMovement();
		} else {
			MoveAction nextMovement = this.steppedMovements.get(progress);
			this.progress++;
		
			return nextMovement;
		}
	}
	
	
	
	
	public int getStepDistance() {
		return stepDistance;
	}
	public void setStepDistance(int stepDistance) {
		this.stepDistance = stepDistance;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public MoveAction getMovement() {
		return movement;
	}

	public void setMovement(MoveAction movement) {
		this.movement = movement;
	}

}
