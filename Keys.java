package com.gmail.username.tylersyme.survivor.window;

import java.util.ArrayList;
import java.util.List;

public class Keys {
public static List<Character> keysPressed = new ArrayList<Character>();
	
	public static void setKeyPressed(Character key, boolean value) {
		if (value == false) {
			keysPressed.remove(key);
		} else {
			keysPressed.add(key);
		}
	}
	
	public static boolean isKeyPressed(Character key) {
		if (keysPressed.contains(key)) {
			return true;
		}
		return false;
	}
}
