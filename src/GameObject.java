import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;

public abstract class GameObject {
	public boolean canRemove = false;
	protected MDGame game;
	protected int width = 35;
	protected int height = 75;
	protected double x, y;
	
	protected void onDeath() {
		canRemove = true;
	}

	public void update() {
		
	}
	
	public void render(Graphics g) {
		
	}
	
	public Area getArea() {
		return new Area(new Rectangle((int)x-width/2, (int)y-height, width, height));
		//return new Rectangle((int)x-width/2, (int)y-height, width, height);
	}
	
	public void hit() {
		onDeath();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public int getHeight() {
		return height;
	}
}
