package com.elieder.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import com.elieder.main.Game;
import com.elieder.world.AStar;
import com.elieder.world.Camera;
import com.elieder.world.FloorTile;
import com.elieder.world.Node;
import com.elieder.world.Vector2i;
import com.elieder.world.WallTile;
import com.elieder.world.World;

public class Enemy extends Entity{
	
	public static final int NORMAL = 0;
	public static final int SCARED = 1;
	public static final int HURTING_PLAYER = 2;
	public static final int WAITING = 3;
	public static final int WANDER = 4;
	public static final int EATEN = 5;
	
	private boolean gameStarted = true;
	
	private double distance;
	
	public boolean ghostMode = false;
	private int ghostFrames = 0;
	public int enemyState;
	
	private boolean right, up, left, down;	
	
	private int speed = 1;
	
	private double waitFrames = 0, timer = 0;
	
	private int scaredFrames = 0, maxScaredFrames = 20, scaredIndex = 0;
	private int eatenFrames = 0, maxEatenFrames = 5, eatenIndex = 0;
	
	public Vector2i originPoint;
	
	public Vector2i target;
			
	private BufferedImage[] spritesNormal;
	private BufferedImage[] spritesScared;
	private BufferedImage[] spritesEaten;
	
	protected List<Node> path;
	
	
 	public Enemy(int x, int y, int width, int height, int speed, BufferedImage[] sprite) {
		super(x, y, width, height, speed, sprite[1]);
		
		this.spritesNormal = sprite;
		this.spritesScared = Entity.SCARED_ENEMY;
		this.spritesEaten = Entity.EATEN_ENEMY;
		
		switch (Game.gameState) {
		
		case Game.START_SCREEN:
			enemyState = WAITING;
			break;
			
		case Game.PLAYING:
			enemyState = NORMAL;
			break;
		}
		
		originPoint = new Vector2i(x, y);
				
	}
		
	public void tick() {
	depth = 1;
	
		switch (Game.gameState) {		
		
		case Game.PLAYING:			
			
			switch (enemyState) {
			
			case WANDER:
				if (new Random().nextInt(100) < 2) enemyState = NORMAL;
				wander();
				break;
				
			case NORMAL:
				distance = getDistance(new Vector2i((int)Game.player.x,(int) Game.player.y), new Vector2i((int) this.x,(int) this.y));
				
				if (new Random().nextInt(100) < 1 && distance > 62) {
					enemyState = WANDER;
				}
						
				target = new Vector2i ((int)(Game.player.x/16), (int)(Game.player.y/16));
				move();
				break;
			
			case SCARED, EATEN:
				target = new Vector2i (originPoint.x/16, originPoint.y/16);
				move();
				ghostTimer();
				break;
				
			case WAITING:
				setTimer(1);
				if (waitTime() == true) setNormal();
				break;
			}
			break;
			
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
				else setWander();
				break;
			
			case WANDER:
				wander();
				break;			
			}	
			break;
		}
	
	}

	public void render(Graphics g) {
		switch (enemyState) {
		
		case NORMAL, HURTING_PLAYER, WAITING, WANDER:
			if (dirX == -1)	g.drawImage(spritesNormal[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
			else if (dirX == 1) g.drawImage(spritesNormal[1], this.getX() - Camera.x, this.getY() - Camera.y, null);			
			else if (dirY == 1) g.drawImage(spritesNormal[2], this.getX() - Camera.x, this.getY() - Camera.y, null);
			else if (dirY == -1) g.drawImage(spritesNormal[3], this.getX() - Camera.x, this.getY() - Camera.y, null);
			else {
				g.drawImage(spritesNormal[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			break;
			
		case SCARED:
			animateScared();
			g.drawImage(spritesScared[scaredIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
			break;
			
		case EATEN:
			animateEaten();		
			g.drawImage(spritesEaten[eatenIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
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
		
		followPath(path);
		
		switch (Game.gameState){
		case Game.PLAYING:
			if (new Random().nextInt(100) < 100) {
				Vector2i start = new Vector2i ((int)(x / 16), (int)(y / 16));	
				Vector2i end = target;	
				path = AStar.findPath(Game.world, start, end);
			}			
		}
		
	}
	
	public void followPath (List<Node> path) {
		if (path != null) {
			if (path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				
				if (x < target.x * 16 && World.isFree((int)(x+speed), this.getY())) {
					x +=speed;
					dirX = 1;
					dirY = 0;
				}else if (x > target.x * 16 && World.isFree((int)(x-speed), this.getY())) {
					x -=speed;
					dirX = -1;
					dirY = 0;
				}
				
				else if (y < target.y * 16 && World.isFree(this.getX(), (int)(y+speed))) {
					y +=speed;
					dirX = 0;
					dirY = 1;
				}else if (y > target.y * 16 && World.isFree(this.getX(), (int)(y-speed))) {
					y -=speed;
					dirX = 0;
					dirY = -1;
				}
				
				if (x == target.x * 16 && y == target.y * 16) {					
					path.remove(path.size() - 1);
				}
			}			
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
				continue;			
			}
			else if (World.tiles[random] instanceof FloorTile) {
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
	
	public void getEaten() {
		enemyState = EATEN;
	}
	
	private static double getDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.x - goal.x;
		double dy = tile.y - goal.y;
		
		return Math.sqrt(dx*dx + dy*dy);
	}	
	
	private void animateScared() {
		scaredFrames++;
		if (scaredFrames == maxScaredFrames) {
			scaredFrames = 0;
			scaredIndex++;
			if (scaredIndex == spritesScared.length - 1) {
				scaredIndex = 0;
			}
			
		}
	}
	
	private void animateEaten() {
		eatenFrames++;
		if (eatenFrames == maxEatenFrames) {
			eatenFrames = 0;
			eatenIndex++;
			if (eatenIndex == spritesEaten.length - 1) {
				eatenIndex = 0;
			}
			
		}
	}
	
	// =========================================	
	
	public void setScared() {
		enemyState = SCARED;
	}
	
	public void setNormal() {
		enemyState = NORMAL;
	}
	
	public void setWander() {
		enemyState = WANDER;
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
