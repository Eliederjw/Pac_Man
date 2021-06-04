package com.elieder.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.elieder.main.Game;
import com.elieder.world.AStar;
import com.elieder.world.Camera;
import com.elieder.world.Vector2i;

public class Enemy extends Entity{	
	
	private final static int NORMAL = 0;
	private final static int SCARED = 1;
	
	public boolean ghostMode = false;
	private static int ghostFrames = 0;	
	public static int enemyState;

 	public Enemy(int x, int y, int width, int height, int speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);	
		
		enemyState = NORMAL;
		
	}
		
	public void tick() {
		
	depth = 1;		
		
//		A_STAR. APLICANDO ALGORITMO PARA ACHAR O CAMINHO MAIS CURTO ATÉ O PLAYER
	
			Vector2i target = new Vector2i ((int)(Game.player.x / 16), (int)(Game.player.y / 16));
			
			if (ghostMode == false) {
				if (path == null || path.size() == 0) {
					Vector2i start = new Vector2i ((int)(x / 16), (int)(y / 16));
					Vector2i end = target;						
					path = AStar.findPath(Game.world, start, end);
					}		
				
				if (new Random().nextInt(100) < 100)
					followPath(path);
				
				if (new Random().nextInt(100) < 5) {
					Vector2i start = new Vector2i ((int)(x / 16), (int)(y / 16));
					Vector2i end = target;
					
					path = AStar.findPath(Game.world, start, end);
				}
			}			
			
			ghostFrames++;
			Game.print(ghostFrames);
//			setGhostMode();	
		}

	public void render(Graphics g) {
		switch (enemyState) {
		case NORMAL:			
			super.render(g);
			break;
		case SCARED:
			g.drawImage(Entity.SCARED_ENEMY, this.getX() - Camera.x, this.getY() - Camera.y, null);			
			break;
		}
		
	}
	
	public void setGhostMode() {
		ghostFrames++;
		if (ghostFrames == 60) {
			ghostFrames = 0;
			if (enemyState == NORMAL) enemyState = SCARED;
			else enemyState = NORMAL;
		}
	}	
	
		
	public static void setStateScared() {
		enemyState = SCARED;
	}
	
	public static void setNormal() {
		enemyState = NORMAL;
	}
	
	

}
