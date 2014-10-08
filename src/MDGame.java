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
	MDMain main;
	
	final public Color BLACK = new Color(5,5,5);
	
	public int groundHeight = 50;
	public double shootAngle = 0, missileSpeed = 0.5;
	long startTime, timer, tempTime;
	public final boolean showBoundingBox = false;
	
	BufferedImage cursorImage;
	
	boolean running = false;

	List<List<GameObject>> objectList = new ArrayList<List<GameObject>>();
	List<GameObject> buildingList = new ArrayList<GameObject>();
	List<GameObject> missileList = new ArrayList<GameObject>();
	List<GameObject> shellList = new ArrayList<GameObject>();
	Turret turret;
	
	Point mouse = MouseInfo.getPointerInfo().getLocation();
	
	// TEMPORARY CLASS USED FOR TESTING
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

	
	//////////// CONSTRUCTORS ////////////
	
	public MDGame() {}
	
	public MDGame(MDMain main) {
		this.main = main;
	}
	

	//////////// GAME INITIATION ////////////
	
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
		running = true;
	}
	
	public void endGame() {
		JOptionPane.showMessageDialog(null, "FAILURE!");
		
	}


	//////////// GAME THREADING ////////////
	
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


	//////////// GAME LOOP ////////////
	
	private void gameLoop() throws InterruptedException {
		while (running) {
			update();
			repaint();
			Thread.sleep(10);
		}
	}


	//////////// GAME LOGIC ////////////
	
	public void update() {
		
		missileSpeed = 0.5 + (0.125 * Math.floor(timer/100000));
		
		if (Math.random()<0.005)
			createMissile();
		
		mouse = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouse, this);
		tempTime = System.currentTimeMillis();
		timer = tempTime - startTime;
		
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
	
	// Creates a missile with a random target building or turret.
	// Note: Angle is currently not working properly. It doesn't always point in the correct direction.
	private void createMissile() {
		int spawnX, spawnY;
		double angle, moveX, moveY;
		Building target;
		
		spawnX = (int)(Math.random()*getWidth());
		spawnY = -25;
		
		if (Math.round(Math.random())*buildingList.size()<1)
			target = (Building)turret;
		else
			target = (Building)buildingList.get((int)Math.round((Math.random()*(buildingList.size()-1))));
			
		angle = Math.abs(Math.atan2(spawnY - target.getY() - target.getHeight()/2, spawnX - target.getX()));
		
		// missileSpeed is a class-level variable and gets higher over time
		moveX = Math.cos(angle)*missileSpeed;
		moveY = Math.sin(angle)*missileSpeed;
		
		missileList.add(new Missile(this,spawnX,spawnY,moveX,moveY,angle));
	}
	
	// Check each missile to see if it collides with something
	public boolean checkCollisions(Missile m) {
		boolean hasCollision = false;
		
		GameObject[] goArray = shellList.toArray(new GameObject[shellList.size()]);
		
		for (GameObject go : goArray) {
			if (m.collides(go.getArea())) {
				go.canRemove = true;
				hasCollision = true;
			}
		}
		
		goArray = buildingList.toArray(new GameObject[buildingList.size()]);
		
		for (GameObject go : goArray) {
			if (m.collides(go.getArea())) {
				Building b = (Building)go;
				b.hit();
				hasCollision = true;
			}
		}
		
		if (m.collides(turret.getArea())) {
			turret.hit();
			hasCollision = true;
		}
		
		if (m.y > getHeight()-groundHeight)
			hasCollision = true;
		
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
		g2d.setColor(BLACK);		
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		// Ground
		g2d.setColor(Color.DARK_GRAY);		
		g2d.fillRect(0, this.getHeight()-groundHeight, this.getWidth(), groundHeight);
		
		// Turret
		turret.render(g2d);
		
		// Shells, Missiles, and Buildings
		for (List<GameObject> list : objectList) {
			for (GameObject obj : list)
				obj.render(g2d);
		}

		////// GUI COMPONENTS //////
		// Timer
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Verdana", Font.BOLD, 20));
		
		// Convert time elapsed to a string with format "HH:mm:MM"
		String timerStr = String.format("%02d:%02d:%02d", 
				TimeUnit.MILLISECONDS.toHours(timer),
				TimeUnit.MILLISECONDS.toMinutes(timer) -  
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timer)),
				TimeUnit.MILLISECONDS.toSeconds(timer) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timer)));
		// We do this in order to center the string no matter the font, size, or length. It gets a rectangle with the size of the font
		Rectangle2D strSize = g2d.getFontMetrics().getStringBounds(timerStr, g2d);

		g2d.drawString(timerStr,(int)(getWidth()/2 - strSize.getWidth()/2), (int)(getHeight()-groundHeight/2 + strSize.getHeight()/2));
		
		String mSpdString = "Missile Speed: " + missileSpeed;
		
		g2d.setFont(new Font("Verdana", Font.BOLD, 10));
		strSize = g2d.getFontMetrics().getStringBounds(mSpdString, g2d);
		g2d.drawString(mSpdString,(int)(getWidth()/2 - strSize.getWidth()/2), (int)(getHeight()-groundHeight*3/4 + strSize.getHeight()/2));
		
		// Cursor
		g2d.drawImage(cursorImage,mouse.x-cursorImage.getWidth()/2,mouse.y-cursorImage.getHeight()/2,null);
		
		// Ammo Indicator
		boolean loaded = (turret.getAmmo() > 0);
		
		int ammo=turret.getAmmo(), maxAmmo=turret.getMaxAmmo(), ammoX=mouse.x-16, ammoY=mouse.y+24;
		
		// Draw "ammo dots" for each shell to indicate how much you have left
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
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
			
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (turret.canShoot())
				shellList.add(turret.shoot());
		} else {
			turret.startReload();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
