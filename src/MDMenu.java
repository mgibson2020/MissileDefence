/* 
 * Program: MDMenu.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: This is the main menu. It provides the high score list and start button.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class MDMenu extends JPanel implements ActionListener {
	MDMain main;
	JButton jbtStart;
	
	public MDMenu(MDMain main, String[] hsNames, long[] hsScores) {
		this.main = main;
		
		// Panel to hold the title
		JPanel pnlTitle = new JPanel();
		pnlTitle.setLayout(new BoxLayout(pnlTitle, BoxLayout.PAGE_AXIS));
		pnlTitle.setBackground(Color.BLACK);
		
		JLabel lblTitle = new JLabel("Missile Defense", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Verdana", Font.BOLD, 50));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);

		// Adding some spacing before and after the title label
		pnlTitle.add(Box.createRigidArea(new Dimension(0,25)));
		pnlTitle.add(lblTitle);
		pnlTitle.add(Box.createRigidArea(new Dimension(0,50)));
		
		// Panel to hold the button/s
		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.PAGE_AXIS));
		jbtStart = new JButton("Click Here to Start Game");
		jbtStart.setFont(new Font("Verdana", Font.BOLD, 30));
		jbtStart.setBackground(Color.BLACK);
		jbtStart.setBorder(null);
		jbtStart.setForeground(Color.WHITE);
		
		// Adding listeners for hovering over the button
		jbtStart.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Sound.play("snd/ButtonHover.wav");
				jbtStart.setFont(new Font("Verdana", Font.BOLD, 35));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				jbtStart.setFont(new Font("Verdana", Font.BOLD, 30));
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
		});
		
		jbtStart.setAlignmentX(CENTER_ALIGNMENT);
		
		// Adding the button/s and spacing to the button panel
		pnlButtons.setBackground(Color.BLACK);
		pnlButtons.add(jbtStart);
		pnlButtons.add(Box.createRigidArea(new Dimension(0,50)));
		pnlButtons.setMinimumSize(new Dimension(0,200));
		
		// Panel to hold the scores
		JPanel pnlScores = new JPanel();
		pnlScores.setBackground(Color.BLACK);
		pnlScores.setForeground(Color.WHITE);
		pnlScores.setLayout(new BoxLayout(pnlScores, BoxLayout.PAGE_AXIS));
		pnlScores.setAlignmentX(CENTER_ALIGNMENT);
		
		// Label to.. label the high scores
		JLabel lblScore = new JLabel("High Scores:", SwingConstants.CENTER);
		lblScore.setFont(new Font("Verdana", Font.BOLD, 25));
		lblScore.setAlignmentX(CENTER_ALIGNMENT);
		lblScore.setForeground(Color.GRAY);

		// Adding high score label to the high score panel
		pnlScores.add(lblScore);
		
		// Adding new labels for each score to the high score panel
		for (int i=0; i<10; i++)
		{		
			// Format the table with fixed-spacing font and fill it with the names and scores of each high score
			JLabel lbl = new JLabel(String.format("%-4s%-25s%s", (i+1) + ".", hsNames[i], main.timeString(hsScores[i])),SwingConstants.CENTER);
			lbl.setFont(new Font("Courier", Font.BOLD, 20));
			lbl.setAlignmentX(CENTER_ALIGNMENT);
			lbl.setForeground(Color.LIGHT_GRAY);
			pnlScores.add(lbl);
		}
		
		// Set the main menu layout and color
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		
		// Add title, button, and score panels to the main menu
		add(pnlTitle,BorderLayout.NORTH);
		add(pnlButtons,BorderLayout.SOUTH);
		add(pnlScores,BorderLayout.CENTER);
		
		// Add a click listener to the button that links to the main menu
		jbtStart.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == jbtStart) {	
			Sound.play("snd/StartGame.wav");
			main.newGame();
		}
	}
}
