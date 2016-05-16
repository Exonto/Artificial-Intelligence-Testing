package com.gmail.username.tylersyme.survivor.window;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.gmail.username.tylersyme.survivor.GameWindow;
import com.gmail.username.tylersyme.survivor.Main;
import com.gmail.username.tylersyme.survivor.Settings;
import com.gmail.username.tylersyme.survivor.entities.Entity;
import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.Tile;
import com.gmail.username.tylersyme.survivor.world.TileType;
import com.gmail.username.tylersyme.survivor.world.World;

/**
 * This class is designed to be paired with {@link GameWindow}.
 * <p>
 * This class will be used to transition from menus to gameplay scenarios. 
 * This will handle the drawing of objects and the handling of mouse and key listeners.
 */
public class GameScene extends JPanel implements KeyListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	
	private World currentWorld = null;

	private GameSceneType sceneType;
	private String title;
	public GameScene(GameSceneType sceneType, String title) {
		this.setSceneType(sceneType);
		this.setTitle(title);
		
		//Add Listeners
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
	}
	
	/**
	 * Paint the scene's components
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (this.currentWorld != null) {
			for (Tile tile : this.currentWorld.worldMap.getLinearMap()) { //Render the world tiles
				if (tile != null && tile.getType() != TileType.AIR) {
					Location loc = tile.getLocation();
					if (loc.sameAs(Main.mouseLocation) == false) {
						if (Main.currentPath == null || Main.currentPath.pathContains(loc) == false && tile.isDebug_Hide() == false) {
							g2.drawImage(tile.getImage(), loc.getRawScreenPoint(true).x, loc.getRawScreenPoint(true).y, Settings.defaultTileSize, Settings.defaultTileSize, null);
						}
					}
					
				}
			}
			
			for (Entity entity : this.currentWorld.getAllEntities()) { //Render the world entities
				if (entity != null) {
					Location loc = entity.getLocation();
					
					g2.drawImage(entity.getImage(), loc.getRawScreenPoint(true).x, loc.getRawScreenPoint(true).y, Settings.defaultTileSize, Settings.defaultTileSize, null);
				}
			}
			
			/*for (Tile tile : this.currentWorld.terrainMap.getLinearMap()) { //Render the world terrain
				if (tile != null && tile.getType() != TileType.AIR) {
					Location loc = tile.getLocation();
					if (loc.sameAs(Main.mouseLocation) == false) {
						if (Main.currentPath == null || Main.currentPath.pathContains(loc) == false) {
							BufferedImage image = tile.getImage();
							g2.drawImage(image, loc.getRawScreenPoint(true).x - (image.getWidth() - 48), loc.getRawScreenPoint(true).y - (image.getHeight() - 32), image.getWidth(), image.getHeight(), null);
						}
					}
					
				}
			}*/
			
			for (Tile tile : this.currentWorld.terrainMap.getLinearMap()) { //Render the world terrain
				if (tile != null && tile.getType() != TileType.AIR) {
					Location loc = tile.getLocation();
					if (loc.sameAs(Main.mouseLocation) == false) {
						if (Main.currentPath == null || Main.currentPath.pathContains(loc) == false) {
							BufferedImage image = tile.getImage();
							g2.drawImage(image, loc.getRawScreenPoint(true).x, loc.getRawScreenPoint(true).y, Settings.defaultTileSize, Settings.defaultTileSize, null);
						}
					}
					
				}
			}
			
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		Character key = e.getKeyChar();
		if (Keys.isKeyPressed(key) == false) {
			Keys.setKeyPressed(key, true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Keys.setKeyPressed(e.getKeyChar(), false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		World world = Main.gameplayScene.getCurrentWorld();
		
		Main.mousePoint = e.getPoint();
		
		Main.mouseLocation = new Location((int) ((e.getPoint().x - world.getCamera().getX()) / Settings.defaultTileSize),
				(int) ((e.getPoint().y - world.getCamera().getY()) / Settings.defaultTileSize));
	}
	

	public GameSceneType getSceneType() {
		return sceneType;
	}

	public void setSceneType(GameSceneType sceneType) {
		this.sceneType = sceneType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public World getCurrentWorld() {
		return currentWorld;
	}

	public void setCurrentWorld(World currentWorld) {
		this.currentWorld = currentWorld;
	}

}
