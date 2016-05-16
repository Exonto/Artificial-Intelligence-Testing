package com.gmail.username.tylersyme.survivor;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.gmail.username.tylersyme.survivor.window.GameScene;

public class GameWindow extends JFrame {
	
	private static final long serialVersionUID = 1L; //UID

	//Default Game Window Setup
	private GameScene currentScene;
	public GameWindow() {
		this.setTitle("The Survivor");
		this.setSize(1080, 760);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Takes the window and centers its location on the screen based on its current size.
	 * <p>
	 * Use this method immediately before displaying the window.
	 */
	public void centerWindow() {
		Dimension centerScreen = Specs.screenCenter;
		Double windowWidth = this.getSize().getWidth();
		Double windowHeight = this.getSize().getHeight();
		
		int adjustedX = centerScreen.width - (windowWidth.intValue() / 2);
		int adjustedY = centerScreen.height - (windowHeight.intValue() / 2);
		
		this.setLocation(adjustedX, adjustedY);
	}

	public GameScene getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(GameScene currentScene) {
		this.currentScene = currentScene;
		this.setContentPane(currentScene);
	}

}
