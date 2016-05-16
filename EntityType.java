package com.gmail.username.tylersyme.survivor.entities;



public enum EntityType {
	
	PLAYER(null),
	CREG(new Creg()),
	SHEEP(new Sheep());
	
	private Entity templateEntity;
	EntityType(Entity templateEntity) {
		this.setTemplateEntity(templateEntity);
	}
	
	/**
	 * Returns a new instance of the entity of this type
	 * @return
	 */
	public Entity getNewEntity() {
		if (this == CREG) {
			return new Creg();
		} else if (this == SHEEP) {
			return new Sheep();
		}
		
		return null;
	}
	
	
	public Entity getTemplateEntity() {
		return templateEntity;
	}
	public void setTemplateEntity(Entity templateEntity) {
		this.templateEntity = templateEntity;
	}

}
