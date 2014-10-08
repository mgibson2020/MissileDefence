import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class Game extends JPanel implements MouseListener {
	public int groundHeight = 50;
	final public Color BLACK = new Color(5,5,5);
	public double shootAngle = 0;
	long startTime;
	long timer;
	long tempTime;
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
		
		Game game = new Game();
		
		frame.add(game);
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		game.startGame();
		
		while (true) {
			game.update();
			game.repaint();
			Thread.sleep(10);
		}
	}
	
	public Game() {}
	
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
		
		missileList.add(new Missile(this,this.getWidth()/2,50,0,0,Math.PI*3/2));
		
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
	}
	
	public void update() {
		mouse = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouse, this);
		tempTime = System.currentTimeMillis();
		timer = tempTime - startTime;
		
		turret.update();
		
		shootAngle = Math.toDegrees(Math.atan2(mouse.y-turret.y+turret.height/2, mouse.x - turret.x));
		
		if (shootAngle > 0) {
			if (shootAngle > 90)
				shootAngle = 180;
			else
				shootAngle = 0;
		}
		
		shootAngle = Math.toRadians(shootAngle);
		
		for (List<GameObject> list : objectList) {
			Iterator<GameObject> iter = list.iterator();
			
			while (iter.hasNext())
			{
				GameObject obj = iter.next();
				obj.update();
				
				if (obj.canRemove)
					iter.remove();
			}
			
			for (GameObject obj : list)
				obj.update();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		boolean done = false;
			
		System.out.println("TEST PAINT");
		
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
		g2d.setFont(new Font("Verdana", Font.BOLD, 30));
		//g2d.drawString(String.valueOf(Math.toDegrees(Math.atan2(mouse.y-turret.y, mouse.x - turret.x))), this.getWidth()/2-50, 30);
		g2d.drawString(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(timer), TimeUnit.MILLISECONDS.toSeconds(timer) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timer))),700,50);
		
		// Cursor
		g2d.drawImage(cursorImage,mouse.x-cursorImage.getWidth()/2,mouse.y-cursorImage.getHeight()/2,null);
		
		// Draw Ammo Indicator
		boolean loaded = (turret.getAmmo() > 0);
		
		int ammo=turret.getAmmo(), maxAmmo=turret.getMaxAmmo(), ammoX=mouse.x-16, ammoY=mouse.y+24;
		
		for (int i=0;i<maxAmmo;i++) {
			loaded = (i < ammo);
			
			if (!loaded) {
				g2d.setColor(Color.GRAY);
			}
			
			g2d.drawOval(ammoX, ammoY, 2, 2);
			
			if (loaded)
				g2d.fillOval(ammoX, ammoY, 2, 2);
			
			ammoX += 8;
		}
		
		if (ammo <= 0)
		{
			g2d.setColor(Color.WHITE);
			
			double reloadTime = turret.getReloadTime(), maxReloadTime = turret.getMaxReloadTime();
			
			g2d.fillRect((int)(mouse.x-16*(reloadTime/maxReloadTime)), ammoY, (int)(36*(reloadTime/maxReloadTime)), 4);
		}
	}
	
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
	
	private void gameLoop() throws InterruptedException {
		while (running) {
			update();
			repaint();
			Thread.sleep(10);
		}
	}

	public void removeFromList(Shell shell) {
		shellList.remove(shell);
	}
	
	public void removeFromList(Building building) {
		buildingList.remove(building);
	}
	
	public void removeFromList(Missile missile) {
		missileList.remove(missile);
	}
	
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
