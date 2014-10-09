/* 
 * Program: GameObject.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: This is the abstract class from which all game objects are derived.
 * It is not implemented directly, but many of its functions do all the work for its children.
 */

import java.awt.*;
import java.awt.geom.Area;

public abstract class GameObject {
	// Whether this can be removed from the game
	public boolean canRemove = false;
	// The game object this is housed in
	protected MDGame game;
	// Width/height 
	protected int width = 35;
	protected int height = 75;
	// Position on the screen
	protected double x, y;
	// Color to draw this with
	protected Color color=Color.WHITE;

	// This class houses the logic steps
	public void update() {}
	
	// This class houses the drawing steps
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		// Get my area for use in both drawing
		Area myArea = getArea();
		
		g2d.setColor(color);
		g2d.draw(myArea);
		g2d.fill(myArea);
		
		if (game.showBoundingBox) {
			g2d.setColor(Color.RED);
			g2d.draw(myArea);
		}
	}
	
	// Returns whether this area collides with another
	public boolean collides(Area a) {
		Area a2 = getArea();
		
		a.intersect(a2);
		
		return !(a.isEmpty());
	}
	
	// If hit by something, the onDeath method is called. Although fairly useless in this case,
	// this method is overridden by the objects that have health to remove it and only call their
	// death when they are out of health
	public void hit() {
		onDeath();
	}
	
	// The actions that must be performed before this object is destroyed
	protected void onDeath() {
		canRemove = true;
	}
	
	// Gets the area of this rectangle for using in both drawing and collision
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
