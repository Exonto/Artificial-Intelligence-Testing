package com.gmail.username.tylersyme.survivor.entities;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.gmail.username.tylersyme.survivor.Main;
import com.gmail.username.tylersyme.survivor.entities.ai.Action;
import com.gmail.username.tylersyme.survivor.entities.ai.Instinct;
import com.gmail.username.tylersyme.survivor.world.Direction;
import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.Tile;

public abstract class Animal extends Entity {

	private List<Instinct> instincts = new ArrayList<Instinct>();
	
	private double maxFood = 50;
	private double currentFood = 50;
	private double foodLossPerSecond = 0.05;
	private double consumeAmountPerSecond = 5.0;
	private double foodDamagePercentage = 0.2; //The percentage point at which health damages the animal
	private double foodDamagePerSecond = 1.0;
	
	private double movementSpeed = 5.0;
	
	private int sightRadius = 10; //The distance at which an animal can perceive its surroundings
	private Location previousFrameLocation; //The location this animal was at the previous frame
	
	private double maxHealth = 100;
	private double currentHealth = 100;
	private List<Tile> visibleTiles = new ArrayList<Tile>(); //Does not include terrain tiles
	public Animal(EntityType type, BufferedImage image) {
		super(type, image);
		
	}
	
	public abstract void update(double elapsed);
	
	public void updateAnimal(double elapsed) {
		updateFood(elapsed);
		updateVisibleTiles();
		
		Action currentAction = this.getCurrentAction();
		if (currentAction != null && currentAction.isCompleted() == false) {
			if (currentAction.isCancelled() == false) {
				this.getCurrentAction().update(elapsed);
			}
		}
		
		this.updateInstincts(elapsed);
	}
	
	public void updateInstincts(double elapsed) {
		if (this.isTrapped()) { //Temporary
			return;
		}
		
		for (Instinct instinct : this.getInstincts()) { //Update the animal's satisfaction level on each instinct
			instinct.update(elapsed);
		}
		
		if (this.getCurrentPriority() != null) { //If there is already a priority
			Instinct currentPriority = this.getCurrentPriority();
			
			Instinct truePriority = this.getPriorityInstinct(); //The actual priority that may have become more important
			
			if (currentPriority.canBeOverriden() == false || truePriority == null || truePriority == currentPriority) {
				currentPriority.solve(elapsed);
			} else {
				currentPriority.end();
			}
		} else { //Determine a new priority
			Instinct priority = this.getPriorityInstinct(); //After updating the instincts's influences, get the (possibly new) highest priority
			if (priority != null) {
				this.setCurrentPriority(priority); //Give the animal a new priority
			}
		}
	}
	
	/**
	 * Updates the animal's food level and will incur the appropriate health effects.
	 * @param elapsed
	 */
	public void updateFood(double elapsed) {
		this.changeFood(((this.getFoodLossPerSecond() * elapsed) / 1000) * -1);
		
		if (this.isStarving()) {
			//this.damage((this.getFoodDamagePerSecond() * elapsed) / 1000);
		}
	}
	
	/**
	 * Updates the current visible tiles by the animal.
	 * This will not include terrain tiles.
	 */
	public void updateVisibleTiles() {
		if (this.getPreviousFrameLocation() == null || this.getLocation().sameAs(this.getPreviousFrameLocation()) == false) { //If the animal has moved since the last frame
			this.setPreviousFrameLocation(this.getLocation());
			this.visibleTiles = Main.gameplayScene.getCurrentWorld().worldMap.getConvexCircle(this.getLocation(), this.getSightRadius());
		}
	}
	
	/**
	 * Returns the currently most important instinct based on the instinct's currentInfluence (aka need);
	 * Will Return null if no instinct has an influence above 0.
	 * @return
	 */
	public Instinct getPriorityInstinct() {
		Instinct priority = null;
		for (Instinct instinct : this.getInstincts()) {
			if (priority == null) {
				if (instinct.getCurrentInfluence() > 0.0) {
					priority = instinct;
				} else {
					continue;
				}
			} else {
				if (instinct.getCurrentInfluence() > priority.getCurrentInfluence() && instinct.getCurrentInfluence() > 0.0) {
					priority = instinct;
				}
			}
		}
		return priority;
	}
	
	public void addInstinct(Instinct instinct) {
		this.getInstincts().add(instinct);
	}
	
	/**
	 * Returns if the animal's food level is at maximum
	 * @return
	 */
	public boolean isFull() {
		return currentFood == maxFood;
	}
	
	/**
	 * Changes the current food this animal has by either a positive or negative amount.
	 * This automatically prevents food from going above maximum capacity or below 0.
	 * @param amount
	 */
	public void changeFood(double amount) {
		this.currentFood += amount;
		if (currentFood > maxFood) {
			currentFood = maxFood;
		} else if (currentFood < 0) {
			currentFood = 0;
		}
	}
	
	/**
	 * Returns if the animal's food is low enough to cause health damage.
	 * @return
	 */
	public boolean isStarving() {
		return (this.getCurrentFood() / this.getMaxFood() <= this.getFoodDamagePercentage());
	}
	
	/**
	 * Returns if the animal's health is less than or equal to 0.
	 * @return
	 */
	public boolean isDead() {
		return this.getHealth() <= 0;
	}
	public boolean isAlive() {
		return isDead() == false;
	}
	
	/**
	 * Changes the current health this animal has by either a positive or negative amount.
	 * This automatically prevents health from going above maximum or below 0.
	 * @param amount
	 */
	private void changeHealth(double amount) {
		this.currentHealth += amount;
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		} else if (currentHealth < 0) {
			currentHealth = 0;
		}
	}
	
	/**
	 * Damages the animal for the given amount.
	 * This will also cause the animal to die if its health drops to zero or below.
	 * @param amount
	 */
	public void damage(double amount) {
		if (amount > 0) amount = -amount; //Ensure the number is negative
		this.changeHealth(amount);
		
		if (this.isDead()) {
			this.kill();
		}
	}
	
	/**
	 * Sets the animal's health to zero and despawns it from the world.
	 */
	public void kill() {
		this.setHealth(0);
		
		this.getWorld().despawnEntity(this);
	}
	
	/**
	 * Returns if the animal is completed enclosed in all directions.
	 * @return
	 */
	public boolean isTrapped() {
		for (Direction dir : Direction.values()) {
			Location relative = dir.getRelative(this.getLocation());
			relative.setWorld(this.getWorld());
			if (relative.getTile() != null && relative.getTile().isPassable()) {
				return false;
			}
		}
		
		return true;
	}
	
	//Getters and Setters

	public List<Instinct> getInstincts() {
		return instincts;
	}

	public void setInstincts(List<Instinct> instincts) {
		this.instincts = instincts;
	}

	public double getMaxFood() {
		return maxFood;
	}

	public void setMaxFood(double maxFood) {
		this.maxFood = maxFood;
	}

	public double getCurrentFood() {
		return currentFood;
	}

	public void setCurrentFood(double currentFood) {
		this.currentFood = currentFood;
	}

	public double getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(double movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public double getFoodLossPerSecond() {
		return foodLossPerSecond;
	}

	public void setFoodLossPerSecond(double foodLossPerSecond) {
		this.foodLossPerSecond = foodLossPerSecond;
	}

	public int getSightRadius() {
		return sightRadius;
	}

	public void setSightRadius(int sightRadius) {
		this.sightRadius = sightRadius;
	}

	public List<Tile> getVisibleTiles() {
		return visibleTiles;
	}

	public void setVisibleTiles(List<Tile> visibleTiles) {
		this.visibleTiles = visibleTiles;
	}

	public double getConsumeAmountPerSecond() {
		return consumeAmountPerSecond;
	}

	public void setConsumeAmountPerSecond(double consumeAmountPerSecond) {
		this.consumeAmountPerSecond = consumeAmountPerSecond;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}

	public double getHealth() {
		return currentHealth;
	}

	public void setHealth(double currentHealth) {
		this.currentHealth = currentHealth;
	}

	public double getFoodDamagePercentage() {
		return foodDamagePercentage;
	}

	public void setFoodDamagePercentage(double foodDamagePercentage) {
		this.foodDamagePercentage = foodDamagePercentage;
	}

	public double getFoodDamagePerSecond() {
		return foodDamagePerSecond;
	}

	public void setFoodDamagePerSecond(double foodDamagePerSecond) {
		this.foodDamagePerSecond = foodDamagePerSecond;
	}

	public Location getPreviousFrameLocation() {
		return previousFrameLocation;
	}

	public void setPreviousFrameLocation(Location previousFrameLocation) {
		this.previousFrameLocation = previousFrameLocation;
	}
	
	
}
