import javax.swing.*;

import java.awt.*;

@SuppressWarnings("serial")
public class MDMain extends JFrame {
	private JPanel deck, mainMenu;
	private MDGame game;
	private CardLayout cardManager;
	
	public MDMain() {
		Container container = getContentPane();
		
		deck = new JPanel();
		cardManager = new CardLayout();
		deck.setLayout(cardManager);
		
		container.add(deck);
		
		mainMenu = new MDMenu(this);
		game = new MDGame(this);
		
		deck.add(mainMenu, "Menu");
		deck.add(game, "Game");
		
		game.setVisible(false);
		
		setResizable(false);
		setVisible(true);
		
	}
	
	public void newGame() {
		try {
			game.startGame();
			
			cardManager.show(deck, "Game");
			
			game.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	public static void main(String[] args) {
		JFrame frame = new MDMain();
		frame.setTitle("Missile Defence");
		frame.setSize(800, 600);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}

