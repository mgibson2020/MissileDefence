
public abstract class Projectile extends GameObject {
	protected double x, y;
	protected double moveX, moveY, angle;
	
	public Projectile(Game game, int x, int y, double moveX, double moveY, double angle) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.moveX = moveX;
		this.moveY = moveY;
		this.angle = angle;
	}
	
	public void update() {
		x += moveX;
		y += moveY;
		
		if (x < -50 || x > game.getWidth()+50 || y < -50 || y > game.getWidth()-game.groundHeight)
			canRemove = true;
	}
	
	private void collision() {
		
	}
	
}
