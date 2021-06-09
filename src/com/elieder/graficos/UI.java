package com.elieder.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.elieder.entities.Player;
import com.elieder.main.Game;

public class UI {
	
	public boolean lostLife = false;
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 18));
//		g.drawString("Food: " + Game.FoodCount + "/" + Game.FoodTotal, 30, 30);
		g.drawString("Score: " + Game.score, 30, 30);
		
		
		for (int i = 0; i < Player.life; i++) {
			g.fillRect(400 + (i*20), 18, 10, 10);
		}
		
	}
	
}
