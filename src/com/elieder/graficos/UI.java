package com.elieder.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.elieder.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 18));
//		g.drawString("Food: " + Game.FoodCount + "/" + Game.FoodTotal, 30, 30);
		g.drawString("Score: " + Game.score, 30, 30);
	}
	
}
