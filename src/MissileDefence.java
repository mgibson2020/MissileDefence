import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MissileDefence extends JPanel {
	//Building testBuild;
	public int groundHeight = 50;
	final public Color BLACK = new Color(5,5,5);

	List<List<GameObject>> objectList = new ArrayList<List<GameObject>>();
	List<GameObject> buildingList = new ArrayList<GameObject>();
	List<GameObject> missileList = new ArrayList<GameObject>();
	List<GameObject> shellList = new ArrayList<GameObject>();
	Turret turret;
	
	Point mouse = MouseInfo.getPointerInfo().getLocation();
	
	public MissileDefence() {
		
	}
	
	public void startGame() {
		objectList.add(shellList);
		objectList.add(buildingList);
		objectList.add(missileList);
		
		int width, xAmount, tX;
		
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
		
		turret.update();
		
		for (List<GameObject> list : objectList) {
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

		g2d.setColor(BLACK);		
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g2d.setColor(Color.DARK_GRAY);		
		g2d.fillRect(0, this.getHeight()-groundHeight, this.getWidth(), groundHeight);
		
		turret.render(g2d);
		
		for (List<GameObject> list : objectList) {
			for (GameObject obj : list)
				obj.render(g2d);
		}

		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Verdana", Font.BOLD, 30));
		g2d.drawString("TEST", this.getWidth()/2-50, 30);
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

}
