package com.elieder.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.elieder.main.Game;

public class Tile {
	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);	
		
	private BufferedImage sprite;
	private double x, y;
	
	public Tile(double x, double y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render (Graphics g) {
		g.drawImage(sprite, (int) x - Camera.x, (int) y - Camera.y, null);			
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;		
	}
}

