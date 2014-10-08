import java.awt.*;
import java.awt.geom.Area;

public abstract class GameObject {
	public boolean canRemove = false;
	protected MDGame game;
	protected int width = 35;
	protected int height = 75;
	protected double x, y;
	protected Color color=Color.WHITE;
	
	protected void onDeath() {
		canRemove = true;
	}

	public void update() {}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		Area myArea = getArea();
		
		g2d.setColor(color);
		g2d.draw(myArea);
		g2d.fill(myArea);
		
		if (game.showBoundingBox) {
			g2d.setColor(Color.RED);
			g2d.draw(myArea);
		}
	}
	
	public boolean collides(Area a) {
		Area a2 = getArea();
		
		a.intersect(a2);
		
		return !(a.isEmpty());
	}
	
	public void hit() {
		onDeath();
	}
	
	public Area getArea() {
		return new Area(new Rectangle((int)x-width/2, (int)y-height, width, height));
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
