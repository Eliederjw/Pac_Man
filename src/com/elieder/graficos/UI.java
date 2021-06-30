package com.elieder.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.elieder.entities.Player;
import com.elieder.main.Game;
import com.elieder.world.World;

public class UI {
	
	public boolean lostLife = false;
	
	private int alpha = 255;
	private boolean alphaOn = false;
	
	private double frames = 0, maxFrames = 0.5;
	
	private int gameOverFrames = 0, maxGameOverTime = 5;
	
	private int colorRate = 1;
	
	private boolean gameOverEntered = false;
	
	public void render(Graphics g) {
			
		switch (Game.gameState) {
		
		case Game.GAME_OVER:
			onEnterGameOver();
			if (gameOverTimer() == false) {
				g.setColor(new Color(255/colorRate, 255/colorRate, 255/colorRate, 255));
				g.setFont(new Font("arial", Font.BOLD, 40));
				g.drawString("Game Over", ((Game.WIDTH*Game.SCALE)/2) - (210/2), 250);
				
			}			
			else {
				World.restartGame();
				Game.gameState = Game.START_SCREEN;
				gameOverEntered = false;
			}
			
			animateGameOver();
			break;
		
		case Game.START_SCREEN:
			g.setColor(new Color(255, 255, 255, alpha));
			g.setFont(new Font("arial", Font.BOLD, 25));
			g.drawString("Press <Enter> to start", ((Game.WIDTH*Game.SCALE)/2) - (260/2), 250);
//			g.drawString("D", 260, 100);
			
			animatePressStart();
			break;
		
		case Game.PLAYING:
	
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, 18));
//		g.drawString("Food: " + Game.FoodCount + "/" + Game.FoodTotal, 30, 30);
			g.drawString("Score: " + Game.score, 30, 30);
			
			
			for (int i = 0; i < Player.life; i++) {
				g.fillRect(400 + (i*20), 18, 10, 10);
				
			}
			break;
		}		
		
	}
	
	public void animatePressStart() {
		
		frames++;
		if(frames == maxFrames * 60) {
			frames = 0;
			if (alphaOn == false) {
				alphaOn = true;
				alpha = 0;
				
			} else if (alphaOn == true) {
				alphaOn = false;
				alpha = 255;
			}		 
		}
		
	}

	public void animateGameOver() {
		frames++;
		if(frames == (maxFrames*2) * 10) {
			frames = 0;
			colorRate++;
			if (colorRate == 5) colorRate = 1;
			
		}
	}
	
	public void onEnterGameOver() {
		if (gameOverEntered == false) {
			gameOverEntered = true;			
			frames = 0;			
		}
	}
	
	public boolean gameOverTimer() {
		gameOverFrames++;
		if (gameOverFrames == maxGameOverTime * 60) {
			gameOverFrames = 0;
			return true;
		}
		return false;
	}
}
