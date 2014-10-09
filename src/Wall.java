/* 
 * Program: Wall.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: Immobile walls. Just used for drawing and collision. Used in the MDGame class.
 */

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Area;

public class Wall extends GameObject {
	public Wall() {}
	
	public Wall(MDGame game, int x, int y, int width, int height, Color color) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	public Area getArea() {
		return new Area(new Rectangle((int)x, (int)y, width, height));
	}
}
