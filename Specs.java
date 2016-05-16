package com.gmail.username.tylersyme.survivor;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Specs {
	
	/*
	 * This class is used to get the host machine's base specifications
	 */
	
	public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final Double width = screenSize.getWidth();
	public static final Double height = screenSize.getHeight();
	
	public static final Dimension screenCenter = new Dimension(width.intValue() / 2, height.intValue() / 2); //The exact center pixel
}
