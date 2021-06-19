package com.elieder.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.elieder.main.Game;
import com.elieder.world.AStar;
import com.elieder.world.Camera;
import com.elieder.world.FloorTile;
import com.elieder.world.Vector2i;
import com.elieder.world.WallTile;
import com.elieder.world.World;

public class Enemy extends Entity{
	
	private final int NORMAL = 0;
	private final int SCARED = 1;
	private final int HURTING_PLAYER = 2;
	private final int WAITING = 3;
	
	private boolean gameStarted = true;
	
	public boolean ghostMode = false;
	private int ghostFrames = 0;
	public int enemyState;
	
	private boolean right, up, left, down;
	
	private int dirX = 0, dirY = 0;
	
	private int speed = 1;
	
	private double waitFrames = 0, timer = 0;
	
	public Vector2i originPoint;
	
	public Vector2i target;
	
 	public Enemy(int x, int y, int width, int height, int speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		enemyState = WAITING;
		originPoint = new Vector2i(x, y);
				
	}
		
	public void tick() {
	depth = 1;
	
		switch (Game.gameState) {
			
		case Game.PLAYING:
			
			switch (enemyState) {
			case NORMAL:
				target = new Vector2i ((int)(Game.player.x / 16), (int)(Game.player.y / 16));
				move();
				break;
			
			case SCARED:
				target = new Vector2i (originPoint.x/16, originPoint.y/16);
				move();
				ghostTimer();
				break;
			case WAITING:
				waitTime();			
			}
			
//==========================================
			
		case Game.START_SCREEN:
			
			switch (enemyState) {
			case WAITING:
				if (gameStarted == true) {
					setRandomPlace();
					setTimer(2);
					gameStarted = false;
				}
				if (waitTime() == false) move();
				else setNormal();
				break;
			
			case NORMAL:		
				wander();
			
			}
		}	
	}

	public void render(Graphics g) {
		switch (enemyState) {
		case NORMAL, HURTING_PLAYER, WAITING:
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
	
	public void wander() {
		if (right && World.isFree((int)(x+speed), this.getY())) {			
			dirX = 1;
			dirY = 0;
		}
		
		else if (left && World.isFree((int)(x-speed), this.getY())) {
			dirX = -1;
			dirY = 0;
		} 
		
		else if (up && World.isFree(this.getX(), (int)(y-speed))) {
			dirY = -1;
			dirX = 0;
		} 
		
		else if (down && World.isFree(this.getX(), (int)(y+speed))) {
			dirY = 1;
			dirX = 0;
		} 
		
		else {
			dirX = 0;
			dirY = 0;
			changeDirection();
		}
				
		x+=speed*dirX;
		y+=speed*dirY;
		
						
	}
	
	public void setRandomPlace() {
		boolean bool = false;
		
		while (bool == false) {
			int random = Entity.rand.nextInt(World.tiles.length);
			if (World.tiles[random] instanceof WallTile) {
				Game.print("WallTile");			
			}
			else if (World.tiles[random] instanceof FloorTile) {
				Game.print("FloorTile");
				Game.print(random);
				bool = true;
				target = new Vector2i ((int)World.tiles[random].getX()/16, (int) World.tiles[random].getY()/16);
			}
		}		
		
	}
	
	public void changeDirection() {
		int random = Entity.rand.nextInt(4);
		
		right = false;
		left = false;
		down = false;
		up = false;
	
		switch (random) {
		
		case 0:
//			if (World.isFree((int)(x+speed), this.getY())) 
			right = true;			
			break;
			
		
		case 1:
//			if (World.isFree((int)(x-speed), this.getY())) 
			left = true;
			break;
		
		case 2:
			up = true;
			break;			
			
		case 3:
			down = true;
			break;
		}
	}
	
	public void repositionEnemy () {
		x = originPoint.x;
		y = originPoint.y;
		path = null;
		setWaiting();
		
	
	}
	
	public void hurtingPlayer() {
		
	}

	public void ghostTimer() {
		ghostFrames++;
		if (ghostFrames == 60*4) {
			enemyState = NORMAL;
			ghostFrames = 0;
		}
	}

	public boolean waitTime() {
			
		waitFrames++;
		if (waitFrames == timer*60) {
			waitFrames = 0;
			timer = 0;
			return true;
		} else return false;
		
	}
	
	// =========================================			
	public void setScared() {
		enemyState = SCARED;
	}
	
	public void setNormal() {
		enemyState = NORMAL;
	}
	
	public void setTimer(int time) {
		timer = time;
	}
	
	public void setWaiting() {
		enemyState = WAITING;		
	}
	
	public void setHurtingPlayer() {
		enemyState = HURTING_PLAYER;
	}

	public int getEnemyState() {
		return enemyState;
	}
}
