package com.gmail.username.tylersyme.survivor.entities;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.gmail.username.tylersyme.survivor.entities.ai.Action;
import com.gmail.username.tylersyme.survivor.entities.ai.Instinct;
import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.World;

public abstract class Entity implements Cloneable {
	
	private Action currentAction; //*Make entirely private that cannot be simply set*
	private Instinct currentPriority;
	
	private World world;
	private boolean isPassable = false;
	
	private EntityType type;
	private BufferedImage image;
	private Location location; //The Entity's Tile Location
	private Location previousLocation; //The Entity's previous tile location
	private Point rawLocation; //The Entity's Raw Pixel Location
	public Entity(EntityType type, BufferedImage image) {
		this.setType(type);
		this.setImage(image);
	}
	
	public abstract void update(double elapsed);
	
	public abstract void initialize();
	
	/**
	 * Returns a new instance of the entity
	 */
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
	
	
	
	
	public EntityType getType() {
		return type;
	}
	public void setType(EntityType type) {
		this.type = type;
	}

	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		if (this.getWorld() != null) {
			location.setWorld(this.getWorld()); //Ensures the world gets passed on
		}
		this.previousLocation = this.location;
		this.location = location;
		
		if (this.isPassable() == false) { //If the entity cannot be walked over
			if (this.previousLocation != null) {
				this.previousLocation.getTile().setPassable(this.previousLocation.getTile().isDefaultPassable()); //Set it's previous tile back to default passability
			}
			this.location.getTile().setPassable(false); //Make the current tile this entity is standing on, not passable
		}
	}

	public Point getRawLocation() {
		return rawLocation;
	}
	public void setRawLocation(Point rawLocation) {
		this.rawLocation = rawLocation;
	}

	public Action getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(Action currentAction) {
		this.currentAction = currentAction;
	}

	public Location getPreviousLocation() {
		return previousLocation;
	}

	public void setPreviousLocation(Location previousLocation) {
		this.previousLocation = previousLocation;
	}

	public Instinct getCurrentPriority() {
		return currentPriority;
	}

	public void setCurrentPriority(Instinct currentPriority) {
		this.currentPriority = currentPriority;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public boolean isPassable() {
		return isPassable;
	}

	public void setPassable(boolean isPassable) {
		this.isPassable = isPassable;
	}

}



















