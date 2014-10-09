/* 
 * Program: Shell.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: Projectiles fired by the player-controlled turret. Interacts with the MDGame class.
 */

public class Shell extends Projectile{
	
	public Shell(MDGame game, int x, int y, double moveX, double moveY, double angle) {
		super(game, x, y, moveX, moveY, angle);
		
		height = 4;
		width = 4;
	}
}
