package com.gmail.username.tylersyme.survivor.entities.ai.instincts;

import com.gmail.username.tylersyme.survivor.entities.Animal;
import com.gmail.username.tylersyme.survivor.entities.ai.Action;
import com.gmail.username.tylersyme.survivor.entities.ai.Instinct;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.ActionType;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.ConsumeAction;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.LocateTileAction;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.movementpatterns.RandomMovementPattern;
import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.TileType;

/**
 * This instinct will be a must have in most if not all animals in the game.
 * This instinct will cause the animal to both search for and consume food based upon the animal's specifications.
 * (Herbivores will find plant life; carnivores will hunt for prey)
 */
public class HungerInstinct extends Instinct {

	public HungerInstinct(Animal animal, int influence) {
		super(animal, influence);
	}

	@Override
	public void update(double elapsed) {
		Animal animal = (Animal) this.getEntity();
		this.setCurrentSatisfaction(this.getSatisfactionRatio(animal.getCurrentFood(),
				animal.getMaxFood())); //Gets the ratio of current food to max food and calculates the resulting satisfaction
	}

	@Override
	public void solve(double elapsed) {
		Animal animal = (Animal) this.getEntity();
		if (this.isSolving() == false) {
			this.setSolving(true);
			
			locateTile(); //Search for the tile
		}
		
		Action currentAction = this.getEntity().getCurrentAction();
		this.setCurrentSolvingAction(currentAction); //Updates this instinct's solving action based upon that of the entity's current action
		
		if (currentAction.isCancelled()) { //If one of the actions was cancelled
			if (currentAction.getType() == ActionType.CONSUME) {
				locateTile();
			}
		}
		
		if (currentAction.isCompleted()) { //If the animal just completed the action
			if (currentAction.getType() == ActionType.LOCATE_TILE) {
				
				LocateTileAction locateFood = (LocateTileAction) currentAction;
				Location foodLocation = locateFood.getLocatedTiles().get(0).getLocation();
				
				this.beginNewAction(new ConsumeAction(true, animal, foodLocation)); //Consume the food that was found
			} else if (currentAction.getType() == ActionType.CONSUME) { //If the animal is now full
				this.end(); //End the instinct action
			}
		}
	}
	
	private void locateTile() {
		Animal animal = (Animal) this.getEntity();
		
		LocateTileAction firstAction = new LocateTileAction(true,
				(Animal) this.getEntity(),
				TileType.PINE_TREE, 
				new RandomMovementPattern(animal.getWorld(), animal, 15)
			);
		
		this.beginNewAction(firstAction);
	}
	
	

}






















