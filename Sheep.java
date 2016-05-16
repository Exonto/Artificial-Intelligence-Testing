package com.gmail.username.tylersyme.survivor.entities;

import com.gmail.username.tylersyme.survivor.ResourceLoader;
import com.gmail.username.tylersyme.survivor.entities.ai.instincts.HungerInstinct;
import com.gmail.username.tylersyme.survivor.entities.ai.instincts.WanderInstinct;

public class Sheep extends Animal {

	public Sheep() {
		super(EntityType.SHEEP, ResourceLoader.getLocalImage("/Sheep.png"));
		
		this.initialize();
	}

	@Override
	public void update(double elapsed) {
		super.updateAnimal(elapsed);
	}

	@Override
	public void initialize() {
		this.getInstincts().clear();
		
		this.setCurrentFood(20);
		this.setFoodLossPerSecond(0.5);
		this.setSightRadius(5);
		
		HungerInstinct hunger = new HungerInstinct(this, 5);
		WanderInstinct wander = new WanderInstinct(this, 12, this.getSightRadius());
		
		this.addInstinct(hunger);
		this.addInstinct(wander);
	}

}
