package com.elieder.entities;

import java.awt.image.BufferedImage;

public class Food extends Entity {

	public Food(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		depth = 5;
	}
	
}
