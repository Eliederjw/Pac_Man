package com.elieder.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.elieder.main.Game;
import com.elieder.world.Camera;
import com.elieder.world.Vector2i;
import com.elieder.world.World;

public class Player extends Entity{
//	Player States
	private final int NORMAL = 0, EATING = 1, GETTING_HURT = 2;
	private int playerState;
	
	public boolean right, up, left, down;
			
	public static int initialLife = 3, life = initialLife;
	
// 	Animation Controlers
	// MaxFrames = Animation Speed
	// index, maxIndex = number of sprites
	private int frames = 0, maxFrames = 15, index = 0, maxIndex = 1;	
	
	private int dieFrames = 0, maxDieFrames = 1;

//	Sprites
	private BufferedImage[] rightSprites;
	private BufferedImage[] leftSprites;

	public Vector2i originPoint;	
	
	public int Dir = 1;
	
	
	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		playerState = NORMAL;			
				
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
		
		switch (Game.gameState) {
		
		case Game.PLAYING:
		
			switch (playerState) {
			case NORMAL:
				move();
				break;
			case GETTING_HURT:
				dieTiming();
				break;
			}
			
			
			checkCollisions();
			animate();
			winTheGame();
			break;							
		}
	}

	public void render(Graphics g) {
		
		switch (Game.gameState) {
		
		case Game.PLAYING:
			if (Dir == 1) {
				g.drawImage(rightSprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			else {
				g.drawImage(leftSprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}	
		
	}

	public void move() {
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
		
	public void stopEnemies() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Enemy) {
				((Enemy) current).setHurtingPlayer();
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
	
	public void checkCollisions() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			
			
			if (current instanceof Food) {
				catchFood(current, i);
			}
			if (current instanceof Strawberry) {
				catchStrawberry(current, i);
			}			
			if (current instanceof Enemy) {
				if (((Enemy) current).getEnemyState() == 0) 
				getHurt(current, i);
			}
			
		}
	}
	
	public void catchFood(Entity entity, int i) {
		if (Entity.isColliding(this, entity)) {
			Game.FoodCount++;
			Game.entities.remove(i);
			Game.score += 10;
			return;
		}
	}

	public void repositionPlayer() {
		x = originPoint.x;
		y = originPoint.y;
	}
		
	public void setOriginPoint(Vector2i originPoint) {
		this.originPoint = originPoint;		
	}
	
	private void catchStrawberry(Entity entity, int i) {
		if (Entity.isColliding(this, entity)) {
			Game.entities.remove(i);
			Game.score += 50;	
			scareEnemies();
			return;
		}
	}
	
	private void getHurt(Entity entity, int i) {
		if (Entity.isColliding(this, entity)) {
			life--;
			playerState = GETTING_HURT;
			stopEnemies();
			if (life <= 0) Game.gameState = Game.GAME_OVER;	
		}
	}
		
	private void dieTiming() {
		dieFrames++;
		if (dieFrames == maxDieFrames * 60) {
			World.repositionElements();
			playerState = NORMAL;
			dieFrames = 0;
		}
	}
	
	private void winTheGame() {
//		Winning the game
	if (Game.FoodCount == Game.FoodTotal) {
		World.restartGame();
		}
	}	
}
