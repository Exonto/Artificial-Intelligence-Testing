package com.gmail.username.tylersyme.survivor.entities.ai.instincts;

import java.util.Random;

import com.gmail.username.tylersyme.survivor.entities.Animal;
import com.gmail.username.tylersyme.survivor.entities.Entity;
import com.gmail.username.tylersyme.survivor.entities.ai.Action;
import com.gmail.username.tylersyme.survivor.entities.ai.Instinct;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.ActionType;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.IdleAction;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.movementpatterns.RandomMovementPattern;

public class WanderInstinct extends Instinct {

	private int wanderRadius; //The max distance at which an animal will wander each iteration
	private double minIdle = 2000.0, maxIdle = 5000.0;
	public WanderInstinct(Entity entity, int influence, int wanderRadius, double minIdle, double maxIdle) {
		super(entity, influence);
		
		this.setWanderRadius(wanderRadius);
		this.setMinIdle(minIdle);
		this.setMaxIdle(maxIdle);
		
		this.setCurrentSatisfaction(90); //Always has a very small desire to wander (cannot be satisfied)
		this.setCanBeOverriden(true); //By default can be overriden
	}
	public WanderInstinct(Entity entity, int influence, int wanderRadius) {
		super(entity, influence);
		
		this.setWanderRadius(wanderRadius);
		
		this.setCurrentSatisfaction(90); //Always has a very small desire to wander (cannot be satisfied)
		this.setCanBeOverriden(true); //By default can be overriden
	}

	@Override
	public void update(double elapsed) { //Satisfaction does not increase or decrease
	
	}

	@Override
	public void solve(double elapsed) {
		Animal animal = (Animal) this.getEntity();
		if (this.isSolving() == false) {
			this.setSolving(true);
			
			goIdle(animal);
		}
		
		Action currentAction = this.getEntity().getCurrentAction();
		
		if (currentAction.isCancelled()) { //If an issue occurred and the action was cancelled
			if (currentAction.getType() == ActionType.MOVE) {
				moveRandomly(animal);
			} else {
				this.end();
			}
		}
		
		if (currentAction.isCompleted()) {
			if (currentAction.getType() == ActionType.IDLE) {
				moveRandomly(animal);
			} else if (currentAction.getType() == ActionType.MOVE) {
				goIdle(animal);
			}
		}
	}

	private void moveRandomly(Animal animal) {
		RandomMovementPattern movePattern = new RandomMovementPattern(animal.getWorld(), animal, this.getWanderRadius());
		
		this.beginNewAction(movePattern.getNextMovement());
	}
	
	private void goIdle(Animal animal) {
		Random r = new Random();
		double idleTimer = this.getMinIdle() + (this.getMaxIdle() - this.getMinIdle()) * r.nextDouble(); //Retrieves a random idle timer
		
		IdleAction idleAction = new IdleAction(true, animal, idleTimer);
		
		this.beginNewAction(idleAction);
	}
	
	
	
	
	
	public int getWanderRadius() {
		return wanderRadius;
	}

	public void setWanderRadius(int wanderRadius) {
		this.wanderRadius = wanderRadius;
	}

	public double getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(double minIdle) {
		this.minIdle = minIdle;
	}

	public double getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(double maxIdle) {
		this.maxIdle = maxIdle;
	}

}




























