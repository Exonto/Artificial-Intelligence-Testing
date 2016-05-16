package com.gmail.username.tylersyme.survivor.world;


public class Camera {

	private double panSpeed = 150.0; //Pixels Per Second
	private double x;
	private double y;
	public Camera(double startX, double startY, double panSpeed) {
		this.setX(startX);
		this.setY(startY);
		this.setPanSpeed(panSpeed);
	}
	

	public double getPanSpeed() {
		return panSpeed;
	}

	public void setPanSpeed(double panSpeed) {
		this.panSpeed = panSpeed;
	}


	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void changeX(double x) {
		this.x += x;
	}

	public void changeY(double y) {
		this.y += y;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
}
