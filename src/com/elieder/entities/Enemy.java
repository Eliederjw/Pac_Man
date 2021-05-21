package com.elieder.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Enemy extends Entity{	
	
	

 	public Enemy(int x, int y, int width, int height, int speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);		
		
	}
		
	public void tick() {
	depth = 0;		
		
//		A_STAR. APLICANDO ALGORITMO PARA ACHAR O CAMINHO MAIS CURTO ATÉ O PLAYER
		/*
		if(this.calculateDistance((int)this.getX(), (int)this.getY(), (int)Game.player.getX(), (int)Game.player.getY()) < 70) {
			
			if (!isCollidingWithPlayer()) {			
				if (path == null || path.size() == 0) {
					Vector2i start = new Vector2i ((int)(x / 16), (int)(y / 16));
					Vector2i end = new Vector2i ((int)(Game.player.x / 16), (int)(Game.player.y / 16));
					
					path = AStar.findPath(Game.world, start, end);
				}
			} else {
				if (new Random().nextInt(100) < 5) {
					Game.player.life -= Game.rand.nextInt(3);
					Game.player.isDamaged = true;
				}
			}
			
			if (new Random().nextInt(100) < 60)
				followPath(path);
			
			if (new Random().nextInt(100) < 5) {
				Vector2i start = new Vector2i ((int)(x / 16), (int)(y / 16));
				Vector2i end = new Vector2i ((int)(Game.player.x / 16), (int)(Game.player.y / 16));
				
				path = AStar.findPath(Game.world, start, end);
			}
		}
		*/	

		}

	public void render(Graphics g) {
						
		}	

}
