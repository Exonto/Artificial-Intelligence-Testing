package com.gmail.username.tylersyme.survivor;

import java.awt.Dimension;
import java.awt.Point;
import java.util.concurrent.TimeUnit;

import com.gmail.username.tylersyme.survivor.entities.EntityType;
import com.gmail.username.tylersyme.survivor.entities.ai.Path;
import com.gmail.username.tylersyme.survivor.window.GameScene;
import com.gmail.username.tylersyme.survivor.window.GameSceneType;
import com.gmail.username.tylersyme.survivor.window.Keys;
import com.gmail.username.tylersyme.survivor.world.Camera;
import com.gmail.username.tylersyme.survivor.world.Location;
import com.gmail.username.tylersyme.survivor.world.TileType;
import com.gmail.username.tylersyme.survivor.world.World;

/**
 * Date Began: 9/23/2015
 * 
 * @version 1.0
 * @author Tyler
 */

public class Main {
	
	public static GameWindow window = new GameWindow(); //The Main JFrame
	public static GameScene gameplayScene = new GameScene(GameSceneType.GAMEPLAY, "World Gameplay");
	
	public static Point mousePoint = new Point(0,0); //Temporary Field
	public static Location mouseLocation = new Location(0,0); //Temporary Field
	public static Path currentPath; //Temporary Field
	
	/**
	 * Start point.
	 * @param args
	 */
	public static void main(String args[]) {
		gameplayScene.setFocusable(true);
		gameplayScene.requestFocus();
		
		World world = new World("First World", new Dimension(40, 40), new Camera(0.0, 0.0, 150.0));
		world.worldMap.loadFlatWorld(TileType.GRASS);
		
		world.worldMap.fill(new Location(1, 7), new Location(15, 7), TileType.DEBUG_BARRIER);
		world.worldMap.fill(new Location(6, 0), new Location(8, 11), TileType.DEBUG_BARRIER);
		
		world.terrainMap.fill(new Location(6, 12), new Location(6, 15), TileType.PINE_TREE);
		
		world.worldMap.fill(new Location(20, 17), new Location(39, 17), TileType.DEBUG_BARRIER);
		
		world.terrainMap.fill(new Location(17, 3), new Location(16, 14), TileType.PINE_TREE);
		
		//world.spawnEntities(EntityType.SHEEP, new Location(0, 0), 5);
		world.spawnEntities(EntityType.CREG, new Location(1, 1), 75);
		
		//world.spawnEntities(EntityType.CREG, new Location(0, 0), 5);
		
		gameplayScene.setCurrentWorld(world);
		
		window.centerWindow();
		window.setCurrentScene(gameplayScene);
		window.setVisible(true);
		
		startGameLoop();
		setGameStarted(true);
	}
	
	private static final double UPDATE_CAP = 10.0; //The pause (in milliseconds) before each update
	/**
	 * This is the game loop that controls and regulates the game's update and rendering speed. 
	 * By pausing this timer, the game will cease to function until resumed.
	 */
	private static void startGameLoop() {
		if (gameStarted) { //Prevents calling this method if the game is already running
			return;
		}
		
		double previous = getCurrentTimeMS(); //Will be used within the loop to store the previous update time stamp
		double accumulated = 0.0; 
		
		double fpsCounter = 0.0;
		int updatesPerSecond = 0;
		while (true) {
			//long time = System.currentTimeMillis();
			
			double current = getCurrentTimeMS(); //Stores the current time stamp
			double elapsed = current - previous; //The amount of time between the previous update and the current update
			accumulated += elapsed; //Accumulates the total amount of elapsed time between *possibly* multiple updates
			fpsCounter += elapsed; //Accumulates the elapsed time in MS until 1 second has passed
			
			previous = current; //Store the current time stamp for the next update
			
			//This loop allows capped FPS control over the program, only letting updates occur every so often rather than at every possible opportunity
			while (accumulated >= UPDATE_CAP) { //If the total accumulation of time between the past updates is now greater than the maximum updates per second
				updateGame(UPDATE_CAP); //Updates the game ahead by exactly X milliseconds
				processInput(); //Handles key strokes
				
				accumulated -= UPDATE_CAP;
				
				updatesPerSecond += 1;
				
				if (fpsCounter >= 1000.0) { //If a second has passed since the previous FPS update
					setFramesPerSecond(updatesPerSecond);
					updatesPerSecond = 0;
					fpsCounter -= 1000;
				}
			}
			renderGame(); //(Not limited by the fps cap for a smoother experience)
		}
	}
	
	/**
	 * Updates the game ahead by the elapsed time
	 * @param elapsed
	 */
	private static void updateGame(double elapsed) {
		Main.gameplayScene.getCurrentWorld().update(elapsed);
	}
	
	private static void processInput() {
		if (window.getCurrentScene().getSceneType() == GameSceneType.GAMEPLAY) {
			World world = window.getCurrentScene().getCurrentWorld();
			if (Keys.isKeyPressed('a')) {
				world.getCamera().changeX(4.0);
			}
			if (Keys.isKeyPressed('d')) {
				world.getCamera().changeX(-4.0);
			} 
			if (Keys.isKeyPressed('w')) {
				world.getCamera().changeY(4.0);
			} 
			if (Keys.isKeyPressed('s')) {
				world.getCamera().changeY(-4.0);
			}
		}
	}
	
	/**
	 * Renders the game to its current update
	 * @param elapsed
	 */
	private static void renderGame() {
		window.repaint(); //Calls the paintComponent() method in the content pane
	}

	/**
	 * Returns the current time stamp in milliseconds from System.nanoTime().
	 * @return
	 */
	public static double getCurrentTimeMS() {
		return TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS); //(double) System.nanoTime() * NANOSECONDS_TO_MILLISECONDS;
	}
	

	private static boolean gameStarted = false;
	public static boolean isGameStarted() { return gameStarted; }
	public static void setGameStarted(boolean gameStarted) { Main.gameStarted = gameStarted; }

	private static int framesPerSecond = 0;
	public static int getFramesPerSecond() { return framesPerSecond; }
	public static void setFramesPerSecond(int framesPerSecond) { Main.framesPerSecond = framesPerSecond; }
}


















