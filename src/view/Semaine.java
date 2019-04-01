package view;

import java.awt.*;
import javax.swing.*;

public class Semaine extends Canvas {

	public void paint(Graphics g){
		Dimension currentSize = getParent().getSize();
		setBackground(Color.DARK_GRAY);
		int width = currentSize.width;
	    int height = currentSize.height;
		g.setColor(Color.WHITE);
		int widthDessin = width - 75;
		int heightDessin = height -20;
		for (int ligne = 0; ligne < 48; ligne++) {
			for(int colonne = 0; colonne < 7; colonne++) {
				g.drawRect(75 + colonne*widthDessin/7, 20 + ligne*heightDessin/48, widthDessin/7, heightDessin/48);
			}
		}
	}
}
	/*private JTable tableau;
	private JScrollPane tableContainer;
	
	public Semaine() {
		super(new FlowLayout(FlowLayout.CENTER));
		
		String  title[] = {"", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
		Object[][] data = { 
				{"00:00", "", "", "", "", "", "", ""},
				{"01:00", "", "", "", "", "", "", ""},
				{"02:00", "", "", "", "", "", "", ""},
				{"03:00", "", "", "", "", "", "", ""},
				{"04:00", "", "", "", "", "", "", ""},
				{"05:00", "", "", "", "", "", "", ""},
				{"06:00", "", "", "", "", "", "", ""},
				{"07:00", "", "", "", "", "", "", ""},
				{"08:00", "", "", "", "", "", "", ""},
				{"09:00", "", "", "", "", "", "", ""},
				{"10:00", "", "", "", "", "", "", ""},
				{"11:00", "", "", "", "", "", "", ""},
				{"12:00", "", "", "", "", "", "", ""},
				{"13:00", "", "", "", "", "", "", ""},
				{"14:00", "", "", "", "", "", "", ""},
				{"15:00", "", "", "", "", "", "", ""},
				{"16:00", "", "", "", "", "", "", ""},
				{"17:00", "", "", "", "", "", "", ""},
				{"18:00", "", "", "", "", "", "", ""},
				{"19:00", "", "", "", "", "", "", ""},
				{"20:00", "", "", "", "", "", "", ""},
				{"21:00", "", "", "", "", "", "", ""},
				{"22:00", "", "", "", "", "", "", ""},
				{"23:00", "", "", "", "", "", "", ""}
		};
		
	    tableau = new JTable(data, title){
	        private static final long serialVersionUID = 1L;

	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
	    };
	    tableContainer = new JScrollPane(tableau);
	    tableau.getTableHeader().setReorderingAllowed(false);
	    tableau.getTableHeader().setResizingAllowed(false);
		this.add(tableContainer, BorderLayout.CENTER);
	}
}
*/