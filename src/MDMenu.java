import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MDMenu extends JPanel implements ActionListener {
	MDMain main;
	JButton jbtStart;
	
	public MDMenu(MDMain main, long[] highScores) {
		this.main = main;
		
		/* You will probably want to give this a layout like borderlayout or gridlayout;
		 * I don't quite remember all the kinds,  but I think the default is FlowLayout
		 * which just kind of fits things wherever it can.
		 */
		
		// Initialize Components
		
		jbtStart = new JButton("Start Game");
		
		// You will need to show a list of the high scores, which are pulled from MDMain in the constructor above
		
		
		// Add Components
		
		add(jbtStart);
		
		
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
