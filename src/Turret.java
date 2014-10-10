/* 
 * Program: Turret.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: This is the player-controlled turret. It is created in (and implements with) the MDGame class.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Turret extends Building {
	// Speed at which the shell travels in the air
	private double shellSpeed = 8;
	private int ammo, maxAmmo, reloadTime, maxReloadTime;
	
	// Constructor
	public Turret(MDGame game) {
		this.game = game;
		health = maxHealth = 6;
		ammo = maxAmmo = 5;
		reloadTime = 0;
		maxReloadTime = 300;
		
		x = game.getWidth()/2;
		y = game.getHeight() - game.groundHeight - 45;
		width = 30;
		height = 30;
	}
	
	// Will return whether the turret is able to shoot or not
	public boolean canShoot() {
		return (ammo > 0);
	}
	
	// Fires a shell in the direction of the mouse
	public Shell shoot() {
		double moveX, moveY, startX, startY;
		
		// Calculate move x and y based off of speed and angle
		moveX = Math.cos(game.shootAngle)*shellSpeed;
		moveY = Math.sin(game.shootAngle)*shellSpeed;
		
		// Spawn it at the end of the barrel (moveX and moveY basically do this for us)
		startX = moveX;
		startY = moveY;
		
		// Remove the ammo and set it to reload if there isn't any left
		ammo--;
		if (ammo <= 0)
			reloadTime = maxReloadTime;
		
		Sound.play("snd/Shoot.wav");
		
		// Create the shell object then return it (so it can be added to the shellList)
		return new Shell(game,(int)(x+startX),(int)(y-height/2+startY),moveX,moveY,game.shootAngle);
	}
	
	// Will begin the reload process prematurely (as long as at least one shell has been fired and it isn't already reloading)
	// This event is called in MDGame when the right (or middle) mouse button is clicked
	public void startReload() {		
		if (reloadTime == 0 && ammo < maxAmmo) {
			ammo = 0;
			reloadTime = maxReloadTime;
		}
	}
	
	// Reloads the ammunition
	private void reload() {
		Sound.play("snd/Reload.wav");
		
		ammo = maxAmmo;
	}
	
	public void update() {
		// When this is "destroyed", the object actually hangs around
		if (health > 0)
		{
			// If in the middle of reloading, reduce the reload timer and reload if able
			if (reloadTime > 0) {
				reloadTime--;
				
				if (reloadTime <= 0)
					reload();
			}
			else
			{
				// Reload time decreases as the difficulty mounts
				maxReloadTime = (int)(300 / game.difficulty);
			}
		}
		else onDeath();
	}
	
	protected void onDeath() {
		super.onDeath();
		game.isEnding = true;
	}
	
	private double healthPercent() {
		return (double)health/maxHealth;
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		////// The Base //////
		g2d.setColor(Color.WHITE);		
		g2d.fillRect((int)x-width/2, (int)y-height/2, width, height/2);
		g2d.fillOval((int)x-width/2, (int)y-height, width, height);
		
		////// The Barrel //////
		// It requires changing the draw transformation, which needs to be reset when done in 
		// order to prevent other objects from being drawn improperly
		AffineTransform oldXForm = g2d.getTransform();
		Rectangle barrel = new Rectangle(5,-3,20,6);

		g2d.translate((int)x, (int)y-(height/2));
		g2d.rotate(game.shootAngle);
		g2d.draw(barrel);
		g2d.fill(barrel);
		
		// Reset the drawing transformation back to normal
		g2d.setTransform(oldXForm);
		
		////// The Health Bar //////
		// Dark bar underneath the health
		g2d.setColor(new Color(100,100,100));	
		g2d.fillRect((int)x-width/2, (int)y+8, width, 8);
		
		// The bar (which becomes more red as it gets closer to zero)
		g2d.setColor(new Color(200,(int)(200*healthPercent()),(int)(200*healthPercent())));
		g2d.fillRect((int)x-width/2, (int)y+8, (int)(width*healthPercent()), 8);
		
		g2d.setColor(new Color(200,200,200));
		g2d.setFont(new Font("Verdana", Font.BOLD, 10));
		g2d.drawString("HP",(int)(x - g2d.getFontMetrics().getStringBounds("HP", g2d).getWidth()/2), (int)y+28);
		
		if (game.showBoundingBox) {
			g.setColor(Color.RED);	
			g2d.draw(getArea().getBounds());
		}
	}
	
	
	public int getAmmo() {
		return ammo;
	}
	
	public int getMaxAmmo() {
		return maxAmmo;
	}
	
	public int getReloadTime() {
		return reloadTime;
	}

	public int getMaxReloadTime() {
		return maxReloadTime;
	}
}
