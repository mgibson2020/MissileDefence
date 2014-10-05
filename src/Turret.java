import java.awt.Graphics;


public class Turret extends Building {
	
	private int ammo;
	private int maxAmmo;
	private int x;
	private int y;
	
	public Turret(int x, int y) {
		super(x,y);
		setHealth(6);
		setMaxHealth(6);
		ammo = 5;
		maxAmmo = 5;
	}
	
	private void onDeath() {
		
	}
	
	private void updLook() {
		
	}
	
	private void reload() {
		
	}
	
	public void setAmmo(int a) {
		
	}
	
	public void getAmmo(int a) {
		
	}
	
	public void update(int u) {
		
	}
	
	public void render(Graphics g) {
		
	}
}
