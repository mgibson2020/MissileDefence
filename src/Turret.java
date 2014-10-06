import java.awt.Color;
import java.awt.Graphics;

public class Turret extends Building {
	private int ammo;
	private int maxAmmo;
	
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
		
	}
	
	public void setAmmo(int a) {
		
	}
	
	public void getAmmo(int a) {
		
	}
	
	public void update() {
		x = game.getWidth()/2;
		y = game.getHeight() - game.groundHeight*2;
		
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
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x-width/2-10, y, width+20, game.groundHeight);
		
		g.setColor(Color.WHITE);		
		g.fillRect(x-width/2, y-height/2, width, height/2);
		g.fillOval(x-width/2, y-height, width, height);
		
		double movex, movey;
		
		movex = Math.cos(game.shootAngle)*20;
		movey = Math.sin(game.shootAngle)*20;
		
		g.fillRect((int)(x+movex-2.5),(int)(y-height/2+movey),5,5);
		
		g.setColor(new Color(100,100,100));	
		g.fillRect(x-width/2, y+8, width, 8);
		
		g.setColor(new Color(200,200,200));
		g.fillRect(x-width/2, y+8, (int)(width*((double)health/maxHealth)), 8);
	}
}
