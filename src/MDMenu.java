import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

@SuppressWarnings("serial")
public class MDMenu extends JPanel implements ActionListener {
	MDMain main;
	JButton jbtStart;
	
	public MDMenu(MDMain main, long[] highScores) {
		this.main = main;
		
		// Initialize Components
		JLabel title = new JLabel("Missile Defense", SwingConstants.CENTER);
		JLabel scores = new JLabel("High Scores \n\n " + highScores[2], SwingConstants.CENTER);
		JPanel panel = new JPanel();
		jbtStart = new JButton("Start Game");
		title.setFont(new Font("Verdana", Font.BOLD, 50));
		title.setForeground(Color.WHITE);
		jbtStart.setFont(new Font("Verdana", Font.BOLD, 30));
		jbtStart.setBackground(Color.BLACK);
		jbtStart.setBorder(null);
		jbtStart.setForeground(Color.WHITE);
		
		// Add Components
		setBackground(Color.BLACK);
		setLayout(new GridLayout(3, 1));
		add(title);
		add(panel);
		panel.setBackground(Color.BLACK);
		panel.add(jbtStart);
		add(scores);
		
		// Add Listeners
		jbtStart.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == jbtStart) {	
			main.newGame();
		}
	}
}
