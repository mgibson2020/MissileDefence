import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MissileDefence extends JPanel {
	Building testBuild;
	
	
	public MissileDefence() {
		testBuild = new Building();
	}
	
	public void update() {
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		testBuild.render(g2d);

		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Verdana", Font.BOLD, 30));
		g2d.drawString("TEST", this.getWidth()/2-50, 30);
	}
	
	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("Missle Defence");
		
		MissileDefence game = new MissileDefence();
		
		frame.add(game);
		frame.setSize(600, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		while (true) {
			game.update();
			game.repaint();
			Thread.sleep(10);
		}
	}

}
