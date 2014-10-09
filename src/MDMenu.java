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
		
		
		
		// Initialize Components
		JPanel pnlTitle = new JPanel();
		pnlTitle.setLayout(new BoxLayout(pnlTitle, BoxLayout.PAGE_AXIS));
		pnlTitle.setBackground(Color.BLACK);
		
		JLabel lblTitle = new JLabel("Missile Defense", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Verdana", Font.BOLD, 50));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);

		pnlTitle.add(Box.createRigidArea(new Dimension(0,25)));
		pnlTitle.add(lblTitle);
		pnlTitle.add(Box.createRigidArea(new Dimension(0,50)));
		
		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.PAGE_AXIS));
		jbtStart = new JButton("Click Here to Start Game");
		jbtStart.setFont(new Font("Verdana", Font.BOLD, 30));
		jbtStart.setBackground(Color.BLACK);
		jbtStart.setBorder(null);
		jbtStart.setForeground(Color.WHITE);
		
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
		
		pnlButtons.setBackground(Color.BLACK);
		pnlButtons.add(jbtStart);
		pnlButtons.add(Box.createRigidArea(new Dimension(0,50)));
		pnlButtons.setMinimumSize(new Dimension(0,200));
		//pnlTitle.add(jbtStart);
		
		JPanel pnlScores = new JPanel();
		pnlScores.setBackground(Color.BLACK);
		pnlScores.setForeground(Color.WHITE);
		pnlScores.setLayout(new BoxLayout(pnlScores, BoxLayout.PAGE_AXIS));
		pnlScores.setAlignmentX(CENTER_ALIGNMENT);
		
		JLabel lblScore = new JLabel("High Scores:", SwingConstants.CENTER);
		lblScore.setFont(new Font("Verdana", Font.BOLD, 25));
		lblScore.setAlignmentX(CENTER_ALIGNMENT);
		lblScore.setForeground(Color.GRAY);

		pnlScores.add(lblScore);
		
		for (int i=0; i<10; i++)
		{		
			JLabel lbl = new JLabel(String.format("%-4s%-25s%s", (i+1) + ".", hsNames[i], main.timeString(hsScores[i])),SwingConstants.CENTER);
			lbl.setFont(new Font("Courier", Font.BOLD, 20));
			lbl.setAlignmentX(CENTER_ALIGNMENT);
			lbl.setForeground(Color.LIGHT_GRAY);
			pnlScores.add(lbl);
		}
		
		// Add Components
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		
		add(pnlTitle,BorderLayout.NORTH);
		add(pnlButtons,BorderLayout.SOUTH);
		add(pnlScores,BorderLayout.CENTER);
		
		// Add Listeners
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
