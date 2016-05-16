package com.gmail.username.tylersyme.survivor.entities.ai.actions.movementpatterns;

import com.gmail.username.tylersyme.survivor.entities.Animal;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.MoveAction;
import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.World;

public class RandomMovementPattern extends MovementPattern {
	
	private int radius; //The max range this pattern can evaluate
	private Location randomLocation;
	public RandomMovementPattern(World world, Animal animal, int radius) {
		super(world, animal);
		
		this.setRadius(radius);
	}

	@Override
	public MoveAction getNextMovement() {
		
		Location randomLocation = null;
		boolean foundPassableLocation = false;
		int searchBuffer = 0; //The maximum number of times this loop will search for an open location within the given parameters
		while(foundPassableLocation == false && searchBuffer < 50) {
			randomLocation = this.getAnimal().getLocation().getRandomLocation(this.getAnimal().getWorld(), this.getRadius());
			this.setRandomLocation(randomLocation);
			
			if (this.getWorld().worldMap.getTile(randomLocation).isPassable()) { //Make sure the tile is passable
				foundPassableLocation = true;
			} else {
				randomLocation = null; //Set the location back to null
			}
			
			searchBuffer++;
		}
		
		if (randomLocation == null) return null; //Couldn't find a passable tile within a number of attempts
		
		MoveAction newMovement = new MoveAction(
				true, 
				this.getAnimal(),
				randomLocation, 
				this.getAnimal().getMovementSpeed());
		
		this.setCurrentMovement(newMovement);
		
		return newMovement;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Location getRandomLocation() {
		return randomLocation;
	}

	private void setRandomLocation(Location randomLocation) {
		this.randomLocation = randomLocation;
	}

}
