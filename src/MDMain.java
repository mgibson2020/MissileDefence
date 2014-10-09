import javax.swing.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("serial")
public class MDMain extends JFrame {
	private JPanel deck, mainMenu;
	private MDGame game;
	private CardLayout cardManager;
	private BufferedReader reader;
	
	public long[] highScores;
	
	public MDMain() throws IOException {
		readHighscores();
		
		Container container = getContentPane();
		
		deck = new JPanel();
		cardManager = new CardLayout();
		deck.setLayout(cardManager);
		
		container.add(deck);
		
		mainMenu = new MDMenu(this, highScores);
		game = new MDGame(this);
		
		deck.add(mainMenu, "Menu");
		deck.add(game, "Game");
		
		game.setVisible(false);
		
		setResizable(false);
		setVisible(true);
	}
	
	public void readHighscores() throws IOException {
		// This will be replaced with a text reader
		highScores = new long[10];
		reader = new BufferedReader(new FileReader("src/scores.txt"));
		long tmp;
		int i = 0;
		while(reader.readLine() != null) {
			tmp = (long)(reader.read());
			highScores[i] = tmp;
			i++;
		}
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
	
	public static void main(String[] args) throws IOException {
		JFrame frame = new MDMain();
		frame.setTitle("Missile Defense");
		frame.setSize(800, 600);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}

