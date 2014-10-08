import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;


public class Missile extends Projectile {
	
	public Missile(MDGame game, int x, int y, double moveX, double moveY, double angle) {
		super(game, x, y, moveX, moveY, angle);

		height = 28;
		width = 6;
	}
	
	public void update() {
		super.update();
		
		if (!canRemove)
			canRemove = game.checkCollisions(this);
	}
	
	public boolean collides(Area a) { //Rectangle rect) {
		Area a2 = getRotatedArea();
		
		a.intersect(a2);
		
		return !(a.isEmpty());
		//return getRotatedBounds().intersects(rect);
	}
	
	public Area getRotatedArea() {
		//Area a = new Area(new Rectangle((int)x, (int)y, length, width));
		Shape rect = new Rectangle((int)x, (int)y, height, width);
		AffineTransform transform = AffineTransform.getRotateInstance(angle, x+height/2, y-width/2);
		
		Area a = new Area(rect);
		a.transform(transform);
		
		return a;
		//return transform.createTransformedShape(rect);
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform oldXForm = g2d.getTransform();
		
		Rectangle shell = new Rectangle(-height,-width/2,height,width);
		
		g2d.setColor(new Color(255,200,150));
		g2d.translate(x,y);
		g2d.rotate(angle);
		g2d.draw(shell);
		g2d.fill(shell);
		
		g2d.setTransform(oldXForm);
		
		if (game.showBoundingBox) {
			g2d.setColor(Color.RED);
			g2d.draw(getRotatedArea());
		}
	}
}
