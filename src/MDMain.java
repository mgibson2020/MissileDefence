/* 
 * Program: MDMain.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: This is the central point of the application, which not only holds the panel that draws the menu or game
 *  but also is where it is launched from. 
 * Game Abstract: A Missile Defense game where the turret must protect six buildings from destruction via missile
 * for as long as it can. The player scores are added to the high score board after they lose.
 * 
 * All sounds made using the free-use program at http://jfxr.frozenfractal.com/
 */

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class MDMain extends JFrame {
	private JPanel deck, menu;
	private MDGame game;
	private CardLayout cardManager;
	
	private String[] hsNames;
	private long[] hsScores;
	
	// Constructor
	public MDMain() throws IOException {
		// Get the high scores from the scores.txt file
		readHighscores();
		
		// Use CardLayout to switch between the menu and the game (and possibly others if they are added in the future)
		Container container = getContentPane();
		
		deck = new JPanel();
		cardManager = new CardLayout();
		deck.setLayout(cardManager);
		
		container.add(deck);
		
		// Create new instances of the menu and game and place them in the "deck"
		menu = new MDMenu(this, hsNames, hsScores);
		game = new MDGame(this, hsScores);
		
		deck.add(menu, "Menu");
		deck.add(game, "Game");
		
		game.setVisible(false);
		
		setResizable(false);
		setVisible(true);
	}
	
	// Creates the window to house everything
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
	
	// Reads the high scores from a .txt file and stores the names and scores into arrays
	public void readHighscores() throws IOException {
		// This will be replaced with a text reader
		hsNames = new String[10];
		hsScores = new long[10];

		int i = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/scores.txt"));
			try {
				String line = null;
				while (i < 10 && (line = reader.readLine()) != null) {
					// Split the line based off of the last comma
					int commaPos = line.lastIndexOf(",");
					hsNames[i] = line.substring(0, commaPos);
					hsScores[i] = Long.parseLong(line.substring(commaPos+1));
					i++;
				}
			} finally {
				reader.close();
			}
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was an error while reading the high scores!\n"+e.getMessage());
		}
	}
	
	// Start a new game
	public void newGame() {
		try {
			game.startGame();
			
			cardManager.show(deck, "Game");
			
			game.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// This is called when the game ends. It updates the scores if needed and refreshes the menu
	public void gameOver(String hsName, long hsScore) {
		// It will only return a high score name if the high score beats at least one record
		// If it does, then insert the score into the array and update the file
		if (hsName != null)
		{
			int hsIndex = 9;
			
			// Finds the place where this score needs to be added (the end if not elements 0 - 8)
			for (int i=0; i<9; i++) {
				if (hsScores[i] < hsScore) {
					hsIndex = i;
					break;
				}
			}
			
			// Moves everything from this position forward (since the highest scores come first)
			for (int i=9; i>hsIndex; i--) {
				hsNames[i] = hsNames[i-1];
				hsScores[i] = hsScores[i-1];
			}
			
			// Update the position with the new name and score
			hsNames[hsIndex] = hsName;
			hsScores[hsIndex] = hsScore;
			
			updateScoreFile();
		
			// We only need to make a new menu if the scores were updated
			deck.remove(menu);
			deck.revalidate();
			
			menu = new MDMenu(this, hsNames, hsScores);
			
			deck.add(menu, "Menu");
		}
		
		// Show the menu
		cardManager.show(deck, "Menu");
	}
	
	// Write the scores to a file
	private void updateScoreFile() {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/scores.txt", false)));
			try {
				for (int i=0; i<10; i++) {
					writer.write(hsNames[i]+","+hsScores[i]);
					writer.newLine();
				}
			} finally {
				writer.close();
			}
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was an error while writing the new high scores!\n"+e.getMessage());
		}
	}
	
	// This method (which I considered making static) converts time in milliseconds to format "mm:ss"
	public String timeString(long time) {
		return String.format("%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(time),
				TimeUnit.MILLISECONDS.toSeconds(time) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}
}

