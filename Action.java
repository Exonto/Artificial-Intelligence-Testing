package com.gmail.username.tylersyme.survivor.entities.ai;

import java.util.List;

import com.gmail.username.tylersyme.survivor.entities.Entity;
import com.gmail.username.tylersyme.survivor.entities.ai.actions.ActionType;
import com.gmail.username.tylersyme.survivor.utility.Hierarchy;

/**
 * This class is used to define an action that may be composed of other actions.
 * An action can be stand alone, but will often contain a series of sub actions in a Hierarchy.
 * Actions will be used to manipulate entity behaviors and decision making.
 * An entity may only perform a single action at any one time, actions can be put on pause until later.
 */
public abstract class Action {

	private Hierarchy actionOrder = new Hierarchy(this); //This contains all the sub actions that are needed to fulfill this action
	//Remember Current Action *IMPORTANT FOR PERFORMANCE*
	
	private ActionType type;
	private Entity entity;
	private boolean isCompleted = false;
	private boolean isCancelled = false;
	private boolean deleteOnCompletion = false; //Determines if the action will automatically removed from the entity after being completely
	private boolean isSuperAction = false; //Used to determine if this action has a parent (super) action
	public Action(boolean isSuperAction, ActionType type, Entity entity) {
		this.setSuperAction(isSuperAction);
		this.setType(type);
		this.setEntity(entity);
	}
	
	/**
	 * Will update the action's overall progress forward {@code elapsed} amount of time.
	 */
	public void update(double elapsed) {
		if (this.isSuperAction() == true) { //All updating has to start from the top. No children may cause themselves to act, only the super action can initiate.
			if (this.isCompleted()) {
				if (this.isDeleteOnCompletion() == false) {
					this.getEntity().getCurrentAction().setCompleted(true); //End the action
				} else {
					this.getEntity().setCurrentAction(null);
				}
			} else {
				Action nextTrueAction = this.getNextTrueAction();
				nextTrueAction.action(elapsed); //Progresses the sub action forward
			}
		}
		
		this.evaluate(); //Allows all actions to evaluate their current situation and be able to morph accordingly
	}
	
	/**
	 * Will progress forward on this specific action's progress.
	 * This contains the "meat" of the action.
	 */
	public abstract void action(double elapsed);
	
	/**
	 * This is the brain of an action. It allows all actions to think and morph according to its surroundings.
	 */
	public abstract void evaluate();
	
	/**
	 * Will add the subAction to the Action's actionOrder hierarchy.
	 * <p>
	 * The order in which you add sub actions <b>does</b> matter.
	 * @param subAction
	 */
	public void addSubAction(Action subAction) {
		this.actionOrder.add(subAction, this);
	}
	
	/**
	 * Returns the next uncompleted subAction from the subActions hierarchy.
	 * Note, this will only return a child of THIS action, not its grand children.
	 * <p>
	 * Will return null if there are no remaining uncompleted/uncancelled children.
	 * @return
	 */
	public Action getNextAction() {
		if (this.hasSubActions() == false) {
			return null;
		}
		
		List<Object> children = actionOrder.getChildren(this);
		for (Object child : children) {
			Action action = (Action) child;
			if (action.isCompleted() == true) { //Will skip past any completed or cancelled actions
				continue;
			} else {
				return action;
			}
		}
		return null; //Every sub action was completed
	}
	
	/**
	 * Returns the next uncompleted subAction from the subActions hierarchy.
	 * Note, this will return the absolute next action, going down through this Action's descendants.
	 * @return
	 */
	public Action getNextTrueAction() {
		Action nextChildAction = this.getNextAction(); //The next child action that hasn't been completed
		
		if (nextChildAction == null) return this;
		
		if (nextChildAction.hasSubActions()) { //Should prevent a null result
			return nextChildAction.getNextTrueAction();
		} else {
			return nextChildAction; //This is the action that has not yet been completed and has no children
		}
	}
	
	/**
	 * Returns if this action has any sub actions
	 * @return
	 */
	public boolean hasSubActions() {
		return this.actionOrder.getChildren(this).size() != 0;
	}
	
	/**
	 * Goes into the entity and cancels the super action by setting isCancelled to true.<p>
	 * An Animal object will not run an action that is cancelled.
	 */
	public void cancelAction() {
		this.setCancelled(true);
		this.setCompleted(false);
	}
	
	public void uncancelAction() {
		this.setCancelled(false);
		this.setCompleted(false);
	}
	
	
	public void removeSubActions() {
		this.actionOrder.clear();
	}
	
	
	//Getters and Setters
	
	public boolean isCompleted() {
		return isCompleted;
	}
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public boolean isSuperAction() {
		return isSuperAction;
	}

	public void setSuperAction(boolean isSuperAction) {
		this.isSuperAction = isSuperAction;
	}

	public Hierarchy getActionOrder() {
		return actionOrder;
	}

	public void setActionOrder(Hierarchy actionOrder) {
		this.actionOrder = actionOrder;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public boolean isDeleteOnCompletion() {
		return deleteOnCompletion;
	}

	/**
	 * Will cause the action to delete itself from its entity immediately after completion.
	 * @param deleteOnCompletion
	 */
	public void setDeleteOnCompletion(boolean deleteOnCompletion) {
		this.deleteOnCompletion = deleteOnCompletion;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	private void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
}
