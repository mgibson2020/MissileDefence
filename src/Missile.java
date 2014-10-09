/* 
 * Program: Missile.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: Projectiles created regularly in the MDGame class with the sole purpose of destroying buildings or the turret.
 */

import java.awt.Color;

public class Missile extends Projectile {
	
	public Missile(MDGame game, int x, int y, double moveX, double moveY, double angle) {
		super(game, x, y, moveX, moveY, angle);

		height = 28;
		width = 6;
		
		color = new Color(255,200,150);
	}
	
	public void update() {
		super.update();
		
		if (!canRemove)
			canRemove = game.checkCollisions(this);
	}
}
