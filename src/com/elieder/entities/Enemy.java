package com.elieder.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.elieder.main.Game;
import com.elieder.world.AStar;
import com.elieder.world.Camera;
import com.elieder.world.Vector2i;

public class Enemy extends Entity{	
	
	public boolean ghostMode = false;
	public int ghostFrames = 0;	

 	public Enemy(int x, int y, int width, int height, int speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);		
		
	}
		
	public void tick() {
	depth = 1;		
		
//		A_STAR. APLICANDO ALGORITMO PARA ACHAR O CAMINHO MAIS CURTO ATÉ O PLAYER		
			if (ghostMode == false) {
				if (path == null || path.size() == 0) {
					Vector2i start = new Vector2i ((int)(x / 16), (int)(y / 16));
					Vector2i end = new Vector2i ((int)(Game.player.x / 16), (int)(Game.player.y / 16));
						
					path = AStar.findPath(Game.world, start, end);	
					}		
				
				if (new Random().nextInt(100) < 100)
					followPath(path);
				
				if (new Random().nextInt(100) < 5) {
					Vector2i start = new Vector2i ((int)(x / 16), (int)(y / 16));
					Vector2i end = new Vector2i ((int)(Game.player.x / 16), (int)(Game.player.y / 16));
					
					path = AStar.findPath(Game.world, start, end);
				}
			}			
			
//			setGhostMode();			
		}

	public void render(Graphics g) {
		if (ghostMode == false)	super.render(g);
		else g.drawImage(Entity.SCARED_ENEMY, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	public void setGhostMode() {
		ghostFrames++;
		if (ghostFrames == 60*4) {
			ghostFrames = 0;
			if (ghostMode == false) ghostMode = true;
			else ghostMode = false;
		}
	}

}
