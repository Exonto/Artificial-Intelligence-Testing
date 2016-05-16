package com.gmail.username.tylersyme.survivor.world;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.gmail.username.tylersyme.survivor.entities.Entity;
import com.gmail.username.tylersyme.survivor.entities.EntityType;
import com.gmail.username.tylersyme.survivor.entities.ai.Pathfinder;

/**
 * This class stores and updates the tile based environment that the current GameScene then renders
 */
public class World {
	
	public TileMap worldMap = null;
	public TileMap terrainMap = null;
	private List<Entity> allEntities = new ArrayList<Entity>();
	
	private String name;
	private Dimension size;
	private Camera camera;
	public World(String name, Dimension size, Camera camera) {
		this.setName(name);
		this.setSize(size);
		this.setCamera(camera);
		
		this.worldMap = new TileMap(this); //Load the empty map
		this.terrainMap = new TileMap(this); //Load the empty terrain map
	}
	
	/**
	 * Updates all tiles and entities
	 * @param elapsed
	 */
	public void update(double elapsed) {
		Pathfinder.processPathRequests(20); //Processes the next 20 path requests
		
		List<Entity> allEntitiesInstance = new ArrayList<Entity>();
		allEntitiesInstance.addAll(this.getAllEntities());
		for (Entity en : allEntitiesInstance) {
			if (this.allEntities.contains(en)) { //In case the entity was despawned
				en.update(elapsed);
			}
		}
	}
	
	/**
	 * Spawns the entity at the given tile location in the world.
	 * @param entity
	 * @param location
	 */
	public void spawnEntity(Entity entity, Location location) {
		if (this.getTile(location).isPassable()) {
			entity.setLocation(new Location(this, location.getX(), location.getY())); //Adds the world into the location
			entity.setWorld(this);
			this.getAllEntities().add(entity);
		} else {
			try {
				throw new Exception("Error: Spawn location is not passable");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void spawnEntity(EntityType type, Location location) {
		Entity entity = type.getNewEntity();
		
		if (this.getTile(location).isPassable()) {
			entity.setLocation(new Location(this, location.getX(), location.getY())); //Adds the world into the location
			entity.setWorld(this);
			this.getAllEntities().add(entity);
		} else {
			try {
				throw new Exception("Error: Spawn location is not passable");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void spawnEntities(EntityType type, Location location, int amount) {
		/*int spawnCount = 0;
		for (int xI = 0; xI < Math.ceil(Math.sqrt(amount)); xI++) {
			for (int yI = 0; yI < Math.ceil(Math.sqrt(amount)); yI++) {
				if (spawnCount >= amount) {
					return;
				}
				
				Location spawnPoint = location.add(xI, yI);
				if (this.getTile(spawnPoint).isPassable()) {
					this.spawnEntity(type, location.add(xI, yI));
				}
				spawnCount++;
			}
		}*/
		int spawnCount = 0;
		for (int xI = 0; xI < Math.ceil(Math.sqrt(amount)) * 2; xI++) {
			for (int yI = 0; yI < Math.ceil(Math.sqrt(amount)) * 2; yI++) {
				if (xI % 2 == 1 || yI % 2 == 1) {
					continue;
				}
				
				if (spawnCount >= amount) {
					return;
				}
				
				Location spawnPoint = location.add(xI, yI);
				if (this.getTile(spawnPoint).isPassable()) {
					this.spawnEntity(type, location.add(xI, yI));
				}
				spawnCount++;
			}
		}
	}
	
	/**
	 * Removes the entity from the world.
	 * @param entity
	 */
	public void despawnEntity(Entity entity) {
		this.getAllEntities().remove(entity);
	}
	
	public Tile getTile(Location location) {
		return worldMap.getTile(location);
	}
	public Tile getTerrainTile(Location location) {
		return terrainMap.getTile(location);
	}
	
	public void setTile(Location location, TileType tileType) {
		this.worldMap.setTile(location, tileType);
	}
	public void setTerrainTile(Location location, TileType tileType) {
		this.terrainMap.setTile(location, tileType);
	}
	
	
	
	//Getters and Setters
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Dimension getSize() {
		return size;
	}
	public void setSize(Dimension size) {
		this.size = size;
	}

	public Camera getCamera() {
		return camera;
	}
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public List<Entity> getAllEntities() {
		return allEntities;
	}
	public void setAllEntities(List<Entity> allEntities) {
		this.allEntities = allEntities;
	}

}
