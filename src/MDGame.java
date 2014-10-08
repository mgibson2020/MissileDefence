import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class MDGame extends JPanel implements MouseListener {
	
////////////INSTANCE VARIABLES ////////////
	MDMain main;
	
	// Debug
	public final boolean showBoundingBox = false;
	
	// Setup
	boolean running = false;
	public int groundHeight = 60;
	BufferedImage cursorImage;
	Turret turret;
	Wall ground, turretStand;
	
	// GameObject lists
	List<List<GameObject>> objectList = new ArrayList<List<GameObject>>();
	List<GameObject> buildingList = new ArrayList<GameObject>();
	List<GameObject> missileList = new ArrayList<GameObject>();
	List<GameObject> shellList = new ArrayList<GameObject>();
	List<GameObject> wallList = new ArrayList<GameObject>();
	
	// Timer variables
	long startTime, timer;
	
	// Missile frequency and difficulty
	public double missileSpeed = 0.25, difficulty = 1;
	public int actionInterval = 5, dcInterval = 30, missilesToSpawn = 1;
	long lastAction = 0, lastDifficultyChange = 0;
	
	// Aiming
	Point mouse = MouseInfo.getPointerInfo().getLocation();
	public double shootAngle = 0;
	
	
	// USED FOR TESTING
	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("GAME TEST - Missle Defence");
		
		MDGame game = new MDGame();
		
		frame.add(game);
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);	
		
		game.startGame();
		
		while (true) {
			game.update();
			game.repaint();
			Thread.sleep(10);
		}
	}
	
////////////CONSTRUCTORS ////////////
	
	public MDGame() {}
	
	public MDGame(MDMain main) {
		this.main = main;
	}
	

////////////GAME INITIATION ////////////
	
	public void startGame() {
		addMouseListener(this);
		// Load the cursor image
		try {
			cursorImage = ImageIO.read(new File("img/cursor.png"));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this.getParent(), "ERROR: Cannot load the cursor image!","Cursor Load Error",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		// Hide the cursor with a blank image
		this.getParent().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB), new Point(0,0), "Shoot Cursor"));
		
		objectList.add(shellList);
		objectList.add(buildingList);
		objectList.add(missileList);
		objectList.add(wallList);
		
		// Stationary Test Missile
		//missileList.add(new Missile(this,this.getWidth()/2,50,0,0,Math.PI*-3/2));
		
		int width, xAmount, tX;
		startTime = System.currentTimeMillis();
		
		width = this.getWidth();
		xAmount = 100;
		tX = -50;
		
		for (int i=1; i<=3; i++)
		{
			buildingList.add(new Building(this,tX + xAmount*i));
			buildingList.add(new Building(this,width - (tX + xAmount*i)));
		}
		
		turret = new Turret(this);
		
		ground = new Wall(this, 0, getHeight()-groundHeight, getWidth(), groundHeight, Color.DARK_GRAY);
		turretStand = new Wall(this, (int)(turret.x-turret.width/2-10),(int)(turret.y),turret.width+20,45, Color.DARK_GRAY);
		
		wallList.add(ground);
		wallList.add(turretStand);
		
		//(int)x-width/2-10, (int)y, width+20, game.groundHeight)
		
		running = true;
	}
	
	public void endGame() {
		JOptionPane.showMessageDialog(null, "FAILURE!");
		
	}


////////////GAME THREADING ////////////
	
	public void run() throws InterruptedException {
		Thread loop = new Thread() {
			public void run() {
				try {
					gameLoop();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		loop.start();
	}


////////////GAME LOOP ////////////
	
	private void gameLoop() throws InterruptedException {
		while (running) {
			update();
			repaint();
			Thread.sleep(10);
		}
	}


////////////GAME LOGIC ////////////
	
	public void update() {
		// This will adjust difficulty and spawn missiles
		enemyActions();
		
		mouse = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouse, this);
		timer = System.currentTimeMillis() - startTime;
		
		shootAngle = Math.toDegrees(Math.atan2(mouse.y-turret.y+turret.height/2, mouse.x - turret.x));
		
		if (shootAngle > 0) {
			if (shootAngle > 90)
				shootAngle = 180;
			else
				shootAngle = 0;
		}
		
		shootAngle = Math.toRadians(shootAngle);
		
		turret.update();
		
		for (List<GameObject> list : objectList) {
			Iterator<GameObject> iter = list.iterator();
			
			while (iter.hasNext())
			{
				GameObject obj = iter.next();
				
				if (!obj.canRemove)
					obj.update();
				
				if (obj.canRemove)
					iter.remove();
			}
			
			for (GameObject obj : list)
				obj.update();
		}
	}
	
	// Spawns missiles and increases difficulty at a regular rate
	private void enemyActions() {	
		// Changing difficulty 
		if (difficulty < 10) {
			// At every difficulty change interval, change the difficulty and skip an action
			if (timer - lastDifficultyChange >= 1000 * dcInterval) {
				difficulty += 0.1;
				lastDifficultyChange = timer;
				lastAction = timer;
			}
		}
		
		// Spawning more missiles
		if (timer - lastAction >= 1000 * actionInterval) {
			// Only spawn AT MOST 3 missiles every action.
			// Also, there cannot be more than 5 missiles at any given time.
			missilesToSpawn = Math.min(3, 5 - missileList.size());
			lastAction = timer;
		}
		else
		{
			// Spawn missiles randomly during this interval
			if (missilesToSpawn > 0) {
				if (Math.random() < 0.005) {
					spawnMissile();
					missilesToSpawn--;
				}
			}
		}
	}
	
	// Creates a missile with a random target building or turret.
	private void spawnMissile() {
		int spawnX, spawnY;
		double angle, moveX, moveY;
		Building target;
		
		spawnX = (int)(Math.random()*getWidth());
		spawnY = -25;
		
		// Either shoot at the turret or one of the buildings. 
		if (Math.round(Math.random())*buildingList.size()<0.5)
			target = (Building)turret;
		else
			target = (Building)buildingList.get((int)Math.round((Math.random()*(buildingList.size()-1))));
		
		// Angle from the missile position to the target
		angle = Math.atan2(target.getY() + target.getHeight()/2 - spawnY, target.getX() - spawnX);
		
		// missileSpeed is a class-level variable and gets higher over time
		moveX = Math.cos(angle)*missileSpeed*difficulty;
		moveY = Math.sin(angle)*missileSpeed*difficulty;
		
		missileList.add(new Missile(this,spawnX,spawnY,moveX,moveY,angle));
	}
	
	// Check each missile to see if it collides with something
	public boolean checkCollisions(Missile m) {
		boolean hasCollision = false;
		
		// Collision with shells
		GameObject[] goArray = shellList.toArray(new GameObject[shellList.size()]);
		
		for (GameObject go : goArray) {
			if (m.collides(go.getArea())) {
				go.canRemove = true;
				hasCollision = true;
			}
		}
		
		// Collision with buildings
		goArray = buildingList.toArray(new GameObject[buildingList.size()]);
		
		for (GameObject go : goArray) {
			if (m.collides(go.getArea())) {
				Building b = (Building)go;
				b.hit();
				hasCollision = true;
			}
		}
		
		// Collision with walls (the ground and turret stand
		goArray = wallList.toArray(new GameObject[wallList.size()]);
		
		for (GameObject go : goArray) {
			if (m.collides(go.getArea())) {
				hasCollision = true;
			}
		}
		
		// Collision with turret
		if (m.collides(turret.getArea())) {
			turret.hit();
			hasCollision = true;
		}
		
		return hasCollision;
	}

	//////////// DRAWING ////////////
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		if (!running) return;
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		////// GAME OBJECTS //////
		// Background
		g2d.setColor(Color.BLACK);		
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		// Ground
		//g2d.setColor(Color.DARK_GRAY);		
		//g2d.fillRect(0, this.getHeight()-groundHeight, this.getWidth(), groundHeight);
		
		// Shells, Missiles, Buildings, and Walls
		for (List<GameObject> list : objectList) {
			for (GameObject obj : list)
				obj.render(g2d);
		}
		
		// Turret
		turret.render(g2d);

		////// GUI COMPONENTS //////
		////// Timer Text
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Verdana", Font.BOLD, 20));
		
		// Convert time elapsed to a string with format "HH:mm:MM"
		String timerStr = String.format("%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(timer),
				TimeUnit.MILLISECONDS.toSeconds(timer) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timer)));
		// We do this in order to center the string no matter the font, size, or length. It gets a rectangle with the size of the font
		Rectangle2D strSize = g2d.getFontMetrics().getStringBounds(timerStr, g2d);

		g2d.drawString(timerStr,(int)(getWidth()/2 - strSize.getWidth()/2), (int)(getHeight()-groundHeight/2 + strSize.getHeight()/2));
		 
		////// Missile Speed Text
		String mSpdString = String.format("SPEED x %.1f", difficulty);
		
		g2d.setFont(new Font("Verdana", Font.BOLD, 10));
		strSize = g2d.getFontMetrics().getStringBounds(mSpdString, g2d);
		g2d.drawString(mSpdString,(int)(getWidth()/2 - strSize.getWidth()/2), (int)(getHeight()-groundHeight*3/4 + strSize.getHeight()/2));

		////// Speed Increase Text
		long lda = timer - lastDifficultyChange;
		
		if (lda < 1500 && timer > 1500)
		{
			if (lda - 1000 >= 0)
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f - (float)(lda - 1000)/500));
			
			String diString = "DIFFICULTY INCREASED!";
			
			g2d.setFont(new Font("Verdana", Font.BOLD, 30));
			strSize = g2d.getFontMetrics().getStringBounds(diString, g2d);
			g2d.drawString(diString,(int)(getWidth()/2 - strSize.getWidth()/2), (int)(getHeight()/2 - strSize.getHeight()/2 - 50 * ((float)lda/1500) ));
			
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
		}
		
		////// Cursor
		g2d.drawImage(cursorImage,mouse.x-cursorImage.getWidth()/2,mouse.y-cursorImage.getHeight()/2,null);
		
		////// Ammo Indicator
		boolean loaded = (turret.getAmmo() > 0);
		
		int ammo=turret.getAmmo(), maxAmmo=turret.getMaxAmmo(), ammoX=mouse.x-16, ammoY=mouse.y+24;
		
		////// Draw "ammo dots" for each shell to indicate how much you have left
		for (int i=0;i<maxAmmo;i++) {
			loaded = (i < ammo);
			
			// If out of ammo at this point, set the color to gray (because it draws from left to right, we do not need logic to change the color to white)
			if (!loaded) {
				g2d.setColor(Color.GRAY);
			}
			
			g2d.drawOval(ammoX, ammoY, 2, 2);
			
			// If this shell has been fired, don't fill it in
			if (loaded)
				g2d.fillOval(ammoX, ammoY, 2, 2);
			
			ammoX += 8;
		}
		
		// Draw reload bar on top of the "ammo dots"
		if (ammo <= 0)
		{
			g2d.setColor(Color.WHITE);
			
			double reloadTime = turret.getReloadTime(), maxReloadTime = turret.getMaxReloadTime();
			
			g2d.fillRect((int)(mouse.x-16*(reloadTime/maxReloadTime)), ammoY, (int)(36*(reloadTime/maxReloadTime)), 4);
		}
	}
	
	
	//////////// REMOVING OBJECTS FROM LISTS (Deletes the object) ////////////
	
	public void removeFromList(Shell shell) {
		shellList.remove(shell);
	}
	
	public void removeFromList(Building building) {
		buildingList.remove(building);
	}
	
	public void removeFromList(Missile missile) {
		missileList.remove(missile);
	}
	
	
	//////////// EVENTS ////////////
	
	// When a mouse button is pressed down, we want to shoot or reload
	// If it was the left button, shoot. Otherwise, reload.
	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (turret.canShoot())
				shellList.add(turret.shoot());
		} else {
			turret.startReload();
		}
	}

	// Unused (required) mouse events
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}

}
