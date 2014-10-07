import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Turret extends Building {
	private int ammo, maxAmmo, reloadTime;
	private int temp=100;
	
	public Turret(MissileDefence game) {
		this.game = game;
		maxHealth = 6;
		health = 6;
		ammo = 5;
		maxAmmo = 5;
		
		width = 30;
		height = 30;
	}
	
	private void onDeath() {
		
	}
	
	private void reload() {
		ammo = maxAmmo;
	}
	
	public boolean canShoot() {
		return (ammo > 0);
	}
	
	public int getAmmo() {
		return ammo;
	}
	
	public Shell shoot() {
		double moveX, moveY, startX, startY;
		
		moveX = Math.cos(game.shootAngle)*2;
		moveY = Math.sin(game.shootAngle)*2;
		startX = moveX*10;
		startY = moveY*10;
		
		ammo--;
		if (ammo <= 0)
			reloadTime = 150;
		
		return new Shell(game,(int)(x+startX),(int)(y-height/2+startY),moveX,moveY);
	}
	
	public void update() {
		x = game.getWidth()/2;
		y = game.getHeight() - game.groundHeight*2;
		
		if (reloadTime > 0) {
			reloadTime--;
			
			if (reloadTime <= 0)
				reload();
		}
		
		temp--;
		
		if (temp<=0)
		{
			temp=100;
			health-=1;
		}
		
		if (health<0)
			health=6;
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(x-width/2-10, y, width+20, game.groundHeight);
		
		g2d.setColor(Color.WHITE);		
		g2d.fillRect(x-width/2, y-height/2, width, height/2);
		g2d.fillOval(x-width/2, y-height, width, height);
		
		AffineTransform oldXForm = g2d.getTransform();
		Rectangle turret = new Rectangle(5,-3,20,6);

		g2d.translate((int)x, (int)y-(height/2));
		g2d.rotate(game.shootAngle);
		g2d.draw(turret);
		g2d.fill(turret);
		
		g2d.setTransform(oldXForm);
		
		g2d.setColor(new Color(100,100,100));	
		g2d.fillRect(x-width/2, y+8, width, 8);
		
		g2d.setColor(new Color(200,200,200));
		g2d.fillRect(x-width/2, y+8, (int)(width*((double)health/maxHealth)), 8);
	}
}
