
public abstract class Projectile extends GameObject {
	protected double x, y;
	protected double moveX, moveY;
	
	public Projectile(MissileDefence game, int x, int y, double moveX, double moveY) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.moveX = moveX;
		this.moveY = moveY;
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
