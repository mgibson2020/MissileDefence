import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class MissileDefence extends JPanel implements MouseListener {
	public int groundHeight = 50;
	final public Color BLACK = new Color(5,5,5);
	public double shootAngle = 0;
	long startTime;
	long timer;
	long tempTime;
	BufferedImage cursorImage;

	List<List<GameObject>> objectList = new ArrayList<List<GameObject>>();
	List<GameObject> buildingList = new ArrayList<GameObject>();
	List<GameObject> missileList = new ArrayList<GameObject>();
	List<GameObject> shellList = new ArrayList<GameObject>();
	Turret turret;
	
	Point mouse = MouseInfo.getPointerInfo().getLocation();
	 
	public MissileDefence() {
		this.addMouseListener(this);
	}
	
	public void startGame() {
		try {
			cursorImage = ImageIO.read(new File("img/cursor.png"));
		}
		catch (Exception e) {}
		
		objectList.add(shellList);
		objectList.add(buildingList);
		objectList.add(missileList);
		
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
		
		shootAngle = Math.toDegrees(Math.atan2(mouse.y-turret.y, mouse.x - turret.x));
		
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
		// Cursor
		g2d.drawImage(cursorImage,mouse.x-cursorImage.getWidth()/2,mouse.y-cursorImage.getHeight()/2,null);
		
		// Timer
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Verdana", Font.BOLD, 30));
		//g2d.drawString(String.valueOf(Math.toDegrees(Math.atan2(mouse.y-turret.y, mouse.x - turret.x))), this.getWidth()/2-50, 30);
		g2d.drawString(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(timer), TimeUnit.MILLISECONDS.toSeconds(timer) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timer))),700,50);
	}
	
	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("Missle Defence");
		
		MissileDefence game = new MissileDefence();
		
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
	public void mousePressed(MouseEvent arg0) {
		if (turret.canShoot())
			shellList.add(turret.shoot());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
