/* 
 * Program: MDGame.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: This is the game screen. This is where all of the game functions take place. 
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
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
	boolean running = false, isEnding=false;
	public int groundHeight = 60;
	BufferedImage cursorImage;
	Turret turret;
	Wall ground, turretStand;
	
	//Sound sound;
	
	// GameObject lists
	List<List<GameObject>> objectList = new ArrayList<List<GameObject>>();
	List<GameObject> buildingList = new ArrayList<GameObject>();
	List<GameObject> missileList = new ArrayList<GameObject>();
	List<GameObject> shellList = new ArrayList<GameObject>();
	List<GameObject> wallList = new ArrayList<GameObject>();
	
	// Timer variables
	long startTime, timer;
	long[] hsScores;
	
	// Missile frequency and difficulty
	public double missileSpeed = 0.5, difficulty = 1;
	public int actionInterval = 5, dcInterval = 60, missilesToSpawn = 1;
	long lastAction = 0, lastDifficultyChange = 0;
	
	// Aiming
	Point mouse = MouseInfo.getPointerInfo().getLocation();
	public double shootAngle = 0;
	
////////////CONSTRUCTORS ////////////
	
	public MDGame() {}
	
	public MDGame(MDMain main, long[] hsScores) {
		this.main = main;
		this.hsScores = hsScores;
	}
	

////////////GAME INITIATION ////////////
	
	public void startGame() {
		addMouseListener(this);
		
		// Load the cursor image
		try {
			cursorImage = ImageIO.read(getClass().getResourceAsStream("img/cursor.png"));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this.getParent(), "ERROR: Cannot load the cursor image!","Cursor Load Error",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		// Hide the cursor with a blank image
		this.getParent().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB), new Point(0,0), "Shoot Cursor"));

		// Add list to the objectList in the order that they should execute
		objectList.add(buildingList);
		objectList.add(wallList);
		objectList.add(shellList);
		objectList.add(missileList);
		
		// Create six buildings, three on each side of the turret
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
		
		// Create the turret in the middle of the screen
		turret = new Turret(this);
		
		// Create the ground and turretStand objects
		ground = new Wall(this, 0, getHeight()-groundHeight, getWidth(), groundHeight, Color.DARK_GRAY);
		turretStand = new Wall(this, (int)(turret.x-turret.width/2-10),(int)(turret.y),turret.width+20,45, Color.DARK_GRAY);
		
		wallList.add(ground);
		wallList.add(turretStand);
		
		running = true;
	}
	
	// Called when the game ends
	public void endGame() {
		// Stopping game thread
		running = false;
		
		// Showing game end message
		String hsName = null;
		
		if (timer > hsScores[9]) {
			while (hsName == null || hsName.length()>20)
			{
				hsName = JOptionPane.showInputDialog(null,"Congratulations! You have earned a new high score!\nPlease enter your name for the high score list. (20 char max)");
				hsName = hsName.trim();
				
				if (hsName.length() > 20)
					JOptionPane.showMessageDialog(null, "Your name cannot be more than 20 characters long.");
					//hsName = hsName.substring(0,30);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Unfortunately, you have not lasted long enough to beat any high scores.\nPlease try again.");
		}
		
		// Setting the cursor back to default
		this.getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		// Telling the main class that the game is over
		main.gameOver(hsName, timer);
	}
	
	public long getNextHighScore(long score) {
		
		for (int i=0; i<10; i++) {
			if (score > hsScores[i]) {
				if (i == 0)
					return score;
				else
					return hsScores[i-1];
			}
		}
		
		return hsScores[9];
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
		// After the fail conditions are met, isEnding will be set to true.
		// When this happens, it will show a message and call endGame after 5 seconds.
		if (isEnding) {
			// End the game 5 seconds after they lose		
			if (System.currentTimeMillis() - startTime - timer > 5000)
				endGame();
			
			if (turret != null)
				if (turret.canRemove)
					turret = null;
		}
		else
		{
			// This will adjust difficulty and spawn missiles
			//enemyActions();
			
			// Get the time that has elapsed since the game started
			// Don't update this if the player has lost, however
			timer = System.currentTimeMillis() - startTime;
		}
		
		// This will adjust difficulty and spawn missiles
		enemyActions();
		
		// Get the current mouse location and convert it to the location relative to the window
		mouse = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouse, this);
		
		if (turret != null)
		{
			// Calculate the shoot angle based off of mouse and turret position
			shootAngle = Math.toDegrees(Math.atan2(mouse.y-turret.y+turret.height/2, mouse.x - turret.x));
			
			// Prevent the angle from pointing downward
			if (shootAngle > 0) {
				if (shootAngle > 90)
					shootAngle = 180;
				else
					shootAngle = 0;
			}
			
			// Convert the angle back to radians
			shootAngle = Math.toRadians(shootAngle);
			
			// Update the turret
			turret.update();
		}
		
		// Update each object in each list
		for (List<GameObject> list : objectList) {
			Iterator<GameObject> iter = list.iterator();
			
			while (iter.hasNext())
			{
				GameObject obj = iter.next();
				
				// Only update it if it is not ready to be removed
				if (!obj.canRemove)
					obj.update();
				
				// If it is ready to be removed, remove it from the list
				if (obj.canRemove)
					iter.remove();
			}
		}
		
		// End the game if all buildings have been destroyed
		if (buildingList.size() < 1)
			isEnding=true;
	}
	
	// Spawns missiles and increases difficulty at a regular rate
	private void enemyActions() {	
		// Changing difficulty 
		if (difficulty < 10) {
			// At every difficulty change interval, change the difficulty and skip an action
			if (timer - lastDifficultyChange >= 1000 * dcInterval) {
				Sound.play("snd/DifficultyIncrease.wav");
				difficulty += 0.5;
				//difficulty += 0.3;
				lastDifficultyChange = timer;
				lastAction = timer;
			}
		}
		
		// Spawning more missiles
		if (timer - lastAction >= 1000 * actionInterval) {
			// After the difficulty first changes, only 1 missile can spawn at a time
			// After 1/3 of the difficulty change duration has passed, 2 missiles can be spawned
			// After 2/3 of the difficulty change duration has passed, 3 missiles can be spawned
			// EX: With difficulty changing every 60 seconds:
			//      At 00 - 19 seconds - 1 missile  maximum
			//		At 20 - 39 seconds - 2 missiles maximum
			//		At 40 - 59 seconds - 3 missiles maximum
			missilesToSpawn = Math.min(1 + (int)((timer - lastDifficultyChange)/(1000*dcInterval/3)),3);
			
			// Only spawn AT MOST 3 missiles at a time
			missilesToSpawn = Math.min(missilesToSpawn, 3 - missileList.size());
			
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
		
		// Spawn the missile in a random location at the top
		spawnX = (int)(Math.random()*getWidth());
		spawnY = -25;
		
		// Either shoot at the turret or one of the buildings. 
		if (turret != null && Math.round(Math.random()*buildingList.size()) < 1)
			target = (Building)turret;
		else if (buildingList.size() > 0)
			target = (Building)buildingList.get((int)Math.round((Math.random()*(buildingList.size()-1))));
		else
			return;
		
		// Angle from the missile position to the target
		angle = Math.atan2(target.getY() + target.getHeight()/2 - spawnY, target.getX() - spawnX);
		
		// missileSpeed is a class-level variable and gets higher over time
		moveX = Math.cos(angle)*missileSpeed*difficulty;
		moveY = Math.sin(angle)*missileSpeed*difficulty;
		
		// Create the missile and it to the missile list
		missileList.add(new Missile(this,spawnX,spawnY,moveX,moveY,angle));
	}
	
	// Check each missile to see if it collides with something
	public boolean checkCollisions(Missile m) {
		boolean hasCollision = false;
		
		// We do not want to check for collision against missiles
		objectList.remove(missileList);
		
		// Check for collision with every object in every list
		for (List<GameObject> list : objectList) {
			GameObject[] goArray = list.toArray(new GameObject[list.size()]);
			
			for (GameObject go : goArray) {
				if (m.collides(go.getArea())) {
					go.hit();
					hasCollision = true;
				}
			}
		}
		
		objectList.add(missileList);
		
		// Check for collision with the turret
		if (turret != null) {
			if (m.collides(turret.getArea())) {
				turret.hit();
				hasCollision = true;
			}
		}
		
		if (hasCollision)
			Sound.play("snd/Explosion.wav");
		
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
		
		// Shells, Missiles, Buildings, and Walls
		for (List<GameObject> list : objectList) {
			for (GameObject obj : list)
				obj.render(g2d);
		}
		
		// Turret
		if (turret != null)
			turret.render(g2d);

		////// GUI COMPONENTS //////
		// I use this string to temporarily hold the string that I need to alter before drawing
		String drawString;
		// This rectangle represents the size of the value held in drawString
		Rectangle2D strSize;
		// GUI Components are all white
		g2d.setColor(Color.WHITE);
		
		////// Missile Speed Text
		drawString = String.format("SPEED x %.1f", difficulty);
		
		g2d.setFont(new Font("Verdana", Font.BOLD, 10));
		strSize = g2d.getFontMetrics().getStringBounds(drawString, g2d);
		g2d.drawString(drawString,(int)(getWidth()/2 - strSize.getWidth()/2), (int)(getHeight()-groundHeight*3/4 + strSize.getHeight()/2));
		
		////// Timer Text
		g2d.setFont(new Font("Verdana", Font.BOLD, 20));
		
		// Convert time elapsed to a string with format "mm:ss"
		drawString = main.timeString(timer);
		
		// We do this in order to center the string no matter the font, size, or length. It gets a rectangle with the size of the font
		strSize = g2d.getFontMetrics().getStringBounds(drawString, g2d);

		g2d.drawString(drawString,(int)(getWidth()/2 - strSize.getWidth()/2), (int)(getHeight()-groundHeight/2 + strSize.getHeight()/2));

		////// Next High Score Text
		// Displays the next high score to beat below your current time
		
		// Most of these will be using the same functions with different values, so I will leave the comments out for repeat lines
		g2d.setFont(new Font("Verdana", Font.BOLD, 10));
		
		drawString = "NEXT HIGH: " + main.timeString(getNextHighScore(timer));
		strSize = g2d.getFontMetrics().getStringBounds(drawString, g2d);
		g2d.drawString(drawString,(int)(getWidth()/2 - strSize.getWidth()/2), (int)(getHeight()-groundHeight*1/4 + strSize.getHeight()/2));

		////// Game Over Text
		// Displays text in the middle of the screen when you meet the fail conditions
		if (isEnding)
		{
			drawString = "GAME OVER - The city is lost!";
			
			g2d.setFont(new Font("Verdana", Font.BOLD, 30));
			strSize = g2d.getFontMetrics().getStringBounds(drawString, g2d);
			g2d.drawString(drawString,(int)(getWidth()/2 - strSize.getWidth()/2), (int)(getHeight()/2 - strSize.getHeight()/2));
		}
		
		////// Speed Increase Text
		// Displays text in the middle of the screen when the difficulty changes
		
		// Amount of time that has passed since the difficulty was adjusted
		long lda = timer - lastDifficultyChange;
		
		if (lda < 1500 && timer > 1500)
		{
			// Starts fading out in the last half second
			if (lda - 1000 >= 0)
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f - (float)(lda - 1000)/500));
			
			drawString = "DIFFICULTY INCREASED!";
			
			// Draws it in the very center of the screen (taking its own width and height into account)
			g2d.setFont(new Font("Verdana", Font.BOLD, 30));
			strSize = g2d.getFontMetrics().getStringBounds(drawString, g2d);
			g2d.drawString(drawString,(int)(getWidth()/2 - strSize.getWidth()/2), (int)(getHeight()/2 - strSize.getHeight()/2 - 50 * ((float)lda/1500) ));
			
			// Sets drawing back to opaque
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
		}
		
		////// Cursor
		// Draws the cursor at the mouse position
		g2d.drawImage(cursorImage,mouse.x-cursorImage.getWidth()/2,mouse.y-cursorImage.getHeight()/2,null);
		
		////// Ammo Indicator
		// Draws the ammunition left at the cursor (as long as the turret still exists)
		if (turret != null) {
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
	}
	
	
	//////////// REMOVING OBJECTS FROM LISTS (Deletes the object) ////////////
	
	public void removeFromList(Shell shell) {
		shellList.remove(shell);
	}
	
	public void removeFromList(Building building) {
		buildingList.remove(building);
		
		Sound.play("snd/BuildingCollapse.wav");
	}
	
	public void removeFromList(Missile missile) {
		missileList.remove(missile);
	}
	
	
	//////////// EVENTS ////////////
	
	// When a mouse button is pressed down, we want to shoot or reload
	// If it was the left button, shoot. Otherwise, reload.
	@Override
	public void mousePressed(MouseEvent e) {
		if (turret !=null) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				if (turret.canShoot())
					shellList.add(turret.shoot());
			} else {
				turret.startReload();
			}
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
