package com.gmail.username.tylersyme.survivor.entities.ai;

import com.gmail.username.tylersyme.survivor.entities.Entity;

/**
 * The base class that calculates the basic needs and desires of all entities and contains the solution.
 */
public abstract class Instinct {
	
	private Action currentSolvingAction;
	private boolean isSolving = false;
	private boolean canBeOverriden = false; //Allows for another instinct that becomes more important, to cancel this instinct's progress
	
	/*
	 * Will be able to pause its progress for a set amount of time if the instinct could not be completed. During this pause, the instinct cannot be acted upon.
	 */
	
	private Entity entity;
	private double influence; //How important this instinct is (the scale is determined by the average influence of all other instincts)
	private double maxSatisfaction = 100; //The point at which the instinct is completely fulfilled
	private double currentSatisfaction = 100;
	public Instinct(Entity entity, int influence) {
		this.setEntity(entity);
		this.setInfluence(influence);
		
	}
	
	/**
	 * Updates the current satisfaction based off its host entity
	 * @param elapsed
	 */
	public abstract void update(double elapsed);
	
	/**
	 * Causes the entity to increase the satisfaction of this instinct.
	 * Returns the action that is being used to solve the priority.
	 * @param elapsed
	 * @return
	 */
	public abstract void solve(double elapsed);
	
	/**
	 * Ends the entity's priority instinct, allowing another instinct to become the priority.
	 */
	public void end() {
		this.setSolving(false);
		
		this.getEntity().setCurrentAction(null);
		this.getEntity().setCurrentPriority(null);
		
		this.setCurrentSolvingAction(null);
	}
	
	/**
	 * Calculates this instinct's current influence.
	 * This is used to compare which instinct is most important at any given time.
	 * @param elapsed
	 */
	public double getCurrentInfluence() {
		return (this.getMaxSatisfaction() - this.getCurrentSatisfaction()) * this.getInfluence();
	}
	
	/**
	 * Returns the ratio between a value and this instinct's satisfaction.<p>
	 * This will allow instincts to regulate their satisfaction levels based off outside influences.
	 * @param currentValue
	 * @param maxValue
	 * @return
	 */
	protected double getSatisfactionRatio(double currentValue, double maxValue) {
		return this.getMaxSatisfaction() * 
				(currentValue / maxValue);
	}
	
	/**
	 * Sets both the instinct and entity's current action
	 * @param action
	 */
	protected void beginNewAction(Action action) {
		this.getEntity().setCurrentAction(action);
		this.setCurrentSolvingAction(action);
	}
	
	
	//Getters and Setters
	
	public double getInfluence() {
		return influence;
	}
	public void setInfluence(double influence) {
		this.influence = influence;
	}

	public double getMaxSatisfaction() {
		return maxSatisfaction;
	}

	public void setMaxSatisfaction(double maxSatisfaction) {
		this.maxSatisfaction = maxSatisfaction;
	}

	public double getCurrentSatisfaction() {
		return currentSatisfaction;
	}

	public void setCurrentSatisfaction(double currentSatisfaction) {
		this.currentSatisfaction = currentSatisfaction;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Action getCurrentSolvingAction() {
		return currentSolvingAction;
	}

	public void setCurrentSolvingAction(Action currentSolvingAction) {
		this.currentSolvingAction = currentSolvingAction;
	}

	public boolean isSolving() {
		return isSolving;
	}

	public void setSolving(boolean isSolving) {
		this.isSolving = isSolving;
	}

	public boolean canBeOverriden() {
		return canBeOverriden;
	}

	/**
	 * Allows for another instinct that becomes more important, to cancel this instinct's progress
	 * @param canBeOverriden
	 */
	public void setCanBeOverriden(boolean canBeOverriden) {
		this.canBeOverriden = canBeOverriden;
	}

}
