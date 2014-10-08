import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MDMenu extends JPanel implements ActionListener {
	MDMain main;
	JButton jbtStart;
	
	public MDMenu(MDMain main) {
		this.main = main;
		
		jbtStart = new JButton("Start Game");
		
		add(jbtStart);
		
		jbtStart.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == jbtStart) {			
			main.newGame();
		}
	}
}
