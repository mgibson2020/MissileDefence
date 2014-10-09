public class Shell extends Projectile{
	
	public Shell(MDGame game, int x, int y, double moveX, double moveY, double angle) {
		super(game, x, y, moveX, moveY, angle);
		
		height = 4;
		width = 4;
	}
}
