import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class MissileDefence extends JFrame implements ActionListener {
	private JPanel deck, mainMenu;
	private Game game;
	private CardLayout cardManager;
	JButton jbtStart;
	
	public MissileDefence() {
		Container container = getContentPane();
		
		deck = new JPanel();
		cardManager = new CardLayout();
		deck.setLayout(cardManager);
		
		container.add(deck);
		
		mainMenu = new JPanel();
		game = new Game();
		
		jbtStart = new JButton("Start Game");
		
		jbtStart.addActionListener(this);
		
		mainMenu.add(jbtStart);
		
		deck.add(mainMenu, "Menu");
		deck.add(game, "Game");
		
		game.setVisible(false);
		
		setResizable(false);
		setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == jbtStart)
		{			
			try {
				game.startGame();
				
				cardManager.show(deck, "Game");
				
				game.run();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new MissileDefence();
		frame.setTitle("Missile Defence");
		frame.setSize(800, 600);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		//Game game = new Game();
		//frame.add(game);
		
		/*game.startGame();
		
		while (true) {
			game.update();
			game.repaint();
			Thread.sleep(10);
		}*/
	}
}

