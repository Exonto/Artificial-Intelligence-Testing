package com.gmail.username.tylersyme.survivor.entities.ai.actions;

import com.gmail.username.tylersyme.survivor.entities.Animal;
import com.gmail.username.tylersyme.survivor.entities.ai.Action;
import com.gmail.username.tylersyme.survivor.world.Location;

public class ConsumeAction extends Action {

	private Location foodLocation;
	public ConsumeAction(boolean isSuperAction, Animal animal, 
			Location foodLocation) {
		super(isSuperAction, ActionType.CONSUME, animal);
		
		this.setFoodLocation(foodLocation);
		
		this.addSubAction(movementNextToFood(animal));
	}

	@Override
	public void action(double elapsed) {
		Animal animal = (Animal) this.getEntity();
		
		if (animal.isFull()) {
			this.setCompleted(true);
		} else {
			animal.changeFood(animal.getConsumeAmountPerSecond() * (elapsed / 1000));
			
			if (animal.isFull()) {
				this.setCompleted(true);
			}
		}
	}
	
	@Override
	public void evaluate() {
		Animal animal = (Animal) this.getEntity();
		MoveAction movement = (MoveAction) this.getNextAction();
		if (movement != null && movement.isCancelled()) { //If this the entity could not find a path to the goal
			this.removeSubActions();
			
			Location openAdjacentFoodLocation = this.getFoodLocation().getClosestOpenAdjacent(animal.getWorld(), animal.getLocation());
			if (openAdjacentFoodLocation != null) {
				MoveAction moveToFood = movementNextToFood((Animal) this.getEntity());
				this.addSubAction(moveToFood);
			} else { //If the path to the food has been blocked
				this.cancelAction(); 
			}
		}
	}
	
	private MoveAction movementNextToFood(Animal animal) {
		
		MoveAction moveToFood = new MoveAction(false, 
				animal, 
				this.getFoodLocation().getClosestOpenAdjacent(animal.getWorld(), animal.getLocation()),
				animal.getMovementSpeed());
		
		return moveToFood;
	}
	
	

	public Location getFoodLocation() {
		return foodLocation;
	}

	public void setFoodLocation(Location foodLocation) {
		this.foodLocation = foodLocation;
	}

}
