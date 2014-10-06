import java.awt.Color;
import java.awt.Graphics;


public class Shell extends Projectile{
	
	public Shell(MissileDefence game, int x, int y, double moveX, double moveY) {
		super(game, x, y, moveX, moveY);
	}

	private void collision() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect((int)x,(int)y,5,5);
	}
}
