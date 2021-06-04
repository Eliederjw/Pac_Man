package com.elieder.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.elieder.main.Game;
import com.elieder.world.AStar;
import com.elieder.world.Camera;
import com.elieder.world.Vector2i;

public class Enemy extends Entity{	
	
	private final  int NORMAL = 0;
	private final  int SCARED = 1;
	
	public boolean ghostMode = false;
	private int ghostFrames = 0;
	public int enemyState;
	
	public Vector2i originPoint; 
	
	public Vector2i target;

 	public Enemy(int x, int y, int width, int height, int speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);	
		
		enemyState = NORMAL;
		originPoint = new Vector2i(x, y);
				
	}
		
	public void tick() {	
	depth = 1;		
		
		Game.print(Game.entities.size());
	
		switch (enemyState) {
		case NORMAL:
			target = new Vector2i ((int)(Game.player.x / 16), (int)(Game.player.y / 16));
			break;
		
		case SCARED:
			target = new Vector2i (originPoint.x/16, originPoint.y/16);
			ghostTimer();
		}
		
	
		move();		
		
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
	
	public void move() {
//		A_STAR. APLICANDO ALGORITMO PARA ACHAR O CAMINHO MAIS CURTO ATÉ O PLAYER
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
	
	public void ghostTimer() {
		ghostFrames++;
		if (ghostFrames == 60*4) {
			enemyState = NORMAL;			
			ghostFrames = 0;
		}
	}	
			
	public void setScared() {
		enemyState = SCARED;
	}
	
	public void setNormal() {
		enemyState = NORMAL;
	}
	
	

}
