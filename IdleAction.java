package com.gmail.username.tylersyme.survivor.entities.ai.actions;

import com.gmail.username.tylersyme.survivor.entities.Entity;
import com.gmail.username.tylersyme.survivor.entities.ai.Action;

public class IdleAction extends Action {

	private double idleTimer; //The total amount (in milliseconds) of time the entity will remain idle
	private double currentIdleTimer; 
	public IdleAction(boolean isSuperAction, Entity entity, double idleTimerMS) {
		super(isSuperAction, ActionType.IDLE, entity);
		
		this.setIdleTimer(idleTimerMS);
		this.setCurrentIdleTimer(0.0);
	}

	@Override
	public void action(double elapsed) {
		this.currentIdleTimer += elapsed;
		
		if (this.getCurrentIdleTimer() > this.getIdleTimer()) { //If the entity has remained idle long enough
			this.setCompleted(true);
		}
		
	}
	
	@Override
	public void evaluate() {
		
	}

	public double getIdleTimer() {
		return idleTimer;
	}

	public void setIdleTimer(double idleTimer) {
		this.idleTimer = idleTimer;
	}

	public double getCurrentIdleTimer() {
		return currentIdleTimer;
	}

	private void setCurrentIdleTimer(double currentIdleTimer) {
		this.currentIdleTimer = currentIdleTimer;
	}

}
