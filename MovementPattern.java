package com.gmail.username.tylersyme.survivor.entities.ai.actions.movementpatterns;

import com.gmail.username.tylersyme.survivor.entities.Animal;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.MoveAction;
import com.gmail.username.tylersyme.survivor.world.World;

public abstract class MovementPattern {
	
	private World world;
	private Animal animal;
	private MoveAction currentMovement;
	public MovementPattern(World world, Animal animal) {
		this.setWorld(world);
		this.setAnimal(animal);
	}

	public abstract MoveAction getNextMovement();

	/*public boolean canProceed(Location nextLocation) {
		
	}*/
	
	

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public MoveAction getCurrentMovement() {
		return currentMovement;
	}

	public void setCurrentMovement(MoveAction currentMovement) {
		this.currentMovement = currentMovement;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	
	
	
}
