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
