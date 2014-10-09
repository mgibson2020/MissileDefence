/* 
 * Program: Building.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: These are the buildings that the player must protect. It is created in (and implements with) the MDGame class.
 */

import java.awt.Color;

public class Building extends GameObject {
	protected int maxHealth = 1;
	protected int health = 1;
	
	public Building() {}
	
	public Building(MDGame game, int x) {
		this.game = game;
		this.x = x;

		maxHealth = health = 3;
		width = 35;
		height = health*15;
		
		color = new Color(150,200,255);
	}
	
	public void update() {
		height = health*15;
		y = game.getHeight() - game.groundHeight;
	}
	
	public void hit() {
		health--;
		
		if (health < 1)
			onDeath();
	}
	
	protected void onDeath() {
		super.onDeath();
		
		Sound.play("snd/BuildingCollapse.wav");
	}
}
