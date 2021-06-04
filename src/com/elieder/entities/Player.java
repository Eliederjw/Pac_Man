package com.elieder.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.elieder.main.Game;
import com.elieder.world.Camera;
import com.elieder.world.World;

public class Player extends Entity{	

	public boolean right, up, left, down;
	
	
// 	Animation Controlers
	// MaxFrames = Animation Speed
	// index, maxIndex = number of sprites 
	private int frames = 0, maxFrames = 15, index = 0, maxIndex = 1;

//	Sprites
	private BufferedImage[] rightSprites;
	private BufferedImage[] leftSprites;

	
	public int Dir = 1;

	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);		
		
//		Carregando sprites
		rightSprites = new BufferedImage[2];
		leftSprites = new BufferedImage[2];
		
		for (int i = 0; i < 2; i++) {
			rightSprites[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);		
		}
		for (int i = 0; i < 2; i++) {
			leftSprites[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);			
		}
				
	}

	public void tick() {
			depth = 0;	
	
	//		MOVIMENTO		
			if (right && World.isFree((int)(x+speed), this.getY())) {
				x+=speed;
				Dir = 1;
			} 
			
			else if (left && World.isFree((int)(x-speed), this.getY())) {
				x-=speed;	
				Dir = -1;
			} 
			
			if (up && World.isFree(this.getX(), (int)(y-speed))) {
				y-=speed;			
			} 
			
			else if (down && World.isFree(this.getX(), (int)(y+speed))) {
				y+=speed;			
			} 
			
			catchFruit();
			
//			Winning the game
			if (Game.FoodCount == Game.FoodTotal) {
				World.restartGame();
			}
			
			animate();			
		}
	
	public void render(Graphics g) {
		if (Dir == 1) {
			g.drawImage(rightSprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		else {
			g.drawImage(leftSprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	
		
	}

	public void animate() {
		frames++;			
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}
	}
		
	public void scareEnemies() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Enemy) {
				((Enemy) current).setScared();
			}
		}
	}
	
	public void catchFruit() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Food) {
				if (Entity.isColliding(this, current)) {
					Game.FoodCount++;
					Game.entities.remove(i);
					Game.score += 10;
					return;
				}
			}
			if (current instanceof Strawberry) {
				if (Entity.isColliding(this, current)) {
					Game.entities.remove(i);
					Game.score += 50;	
					scareEnemies();
					return;
				}
			}
		}
	}
}
