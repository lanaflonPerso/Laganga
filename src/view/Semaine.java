package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Semaine extends JPanel {
	
	public Semaine() {
		this.setLayout(new BorderLayout());
		
		JPanel changerSemaine = new JPanel(new FlowLayout());
		changerSemaine.add(new JButton("Semaine précédente"));
		changerSemaine.add(new JButton("Semaine suivante"));
		
		
		this.add(changerSemaine, BorderLayout.NORTH);
		this.add(new CanevasSemaine(), BorderLayout.CENTER);
	}
}