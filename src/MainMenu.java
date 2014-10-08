import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MainMenu extends JPanel implements ActionListener {
	JButton jbtStart;
	
	public MainMenu() {
		jbtStart = new JButton("Start Game");
		
		add(jbtStart);
		
		jbtStart.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == jbtStart)
		{			
			
		}
	}
}
