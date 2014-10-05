
public abstract class Projectile extends GameObject {
	
	public int speed;
	public long directionX;
	public long directionY;
	
	private long calcMoveX() {
		return directionX;
	}
	
	private long calcMoveY() {
		return directionY;
	}
	
	private void updLook() {
		
	}
	
	private void collision() {
		
	}
	
}
