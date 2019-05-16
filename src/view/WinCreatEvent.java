package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.toedter.calendar.JCalendar;

import controller.AnnuleEvent;
import controller.CloseWindow;
import controller.CreateEventListener;
import controller.SearchUser;
import model.entities.Entity;
import model.entities.User;

public class WinCreatEvent extends JFrame implements Observer {

	private JLabel status = new JLabel("Ajouter un événement", JLabel.CENTER);
	private JTextField textName;
	private JTextArea textDescription;
	private JCalendar calendarBegin;
	private JCalendar calendarEnd;
	private JSpinner hourBegin;
	private JSpinner hourEnd;
	private JSpinner minuteBegin;
	private JSpinner minuteEnd;
				
	public WinCreatEvent() throws Exception {
		super();
				
		this.setTitle("Création événement");
	    this.setSize(1000, 700);
		this.setBackground(Color.white);
		this.setLayout(new BorderLayout());
		
		this.add(status, BorderLayout.NORTH);
		
		//Panel au centre de la fenêtre
		JPanel newEvent = new JPanel(new FlowLayout());
		
		//Panel pour le nom de l'événement
		JPanel name = new JPanel(new GridLayout(1,2));
		JLabel labelName = new JLabel("Nom de l'événement : ");
		textName = new JTextField();
		textName.setPreferredSize(new Dimension(370, 30));
		name.add(labelName);
		name.add(textName);
		
		//Panel début de l'événement
		JPanel dateBegin = new JPanel(new GridLayout(1,3));
		
		//Date
		JLabel labelBegin = new JLabel("Début de l'événement : ");
		calendarBegin = new JCalendar();
		
		//Heure
		JPanel timeBegin = new JPanel(new FlowLayout());
		hourBegin = new JSpinner(new SpinnerNumberModel(00, 00, 23, 1));
		minuteBegin = new JSpinner(new SpinnerNumberModel(00, 00, 59, 1));
		timeBegin.add(hourBegin);
		timeBegin.add(minuteBegin);
		
		dateBegin.add(labelBegin);
		dateBegin.add(calendarBegin);
		dateBegin.add(timeBegin);
		
		//Panel fin de l'événement
		JPanel dateEnd = new JPanel(new GridLayout(1,3));
		//Date
		JLabel labelEnd = new JLabel("Fin de l'événement : ");
		calendarEnd = new JCalendar();
		
		//Heure
		JPanel timeEnd = new JPanel(new FlowLayout());
		hourEnd = new JSpinner(new SpinnerNumberModel(00, 00, 23, 1));
		minuteEnd = new JSpinner(new SpinnerNumberModel(00, 00, 59, 1));
		timeEnd.add(hourEnd);
		timeEnd.add(minuteEnd);
		
		dateEnd.add(labelEnd);
		dateEnd.add(calendarEnd);
		dateEnd.add(timeEnd);
		
		//Panel description
		JPanel description = new JPanel(new GridLayout(1,2));
		JLabel labelDescription = new JLabel("Description de l'énévement : ");
		textDescription = new JTextArea();
		textDescription.setPreferredSize(new Dimension(400, 40));
		description.add(labelDescription);
		description.add(textDescription);
		
		//panel membres participant aux evenements
		JPanel participants = new JPanel(new GridLayout(1,2)); //paneau qui permet de séparer en deux partie le GridLayout
		participants.setPreferredSize(new Dimension(800, 130));
		JLabel labelParticipants = new JLabel("Membres parcipants à l'événement : "); 
		
		JPanel panMembres = new JPanel(new GridLayout(1,2)); 
		JPanel search = new JPanel(new BorderLayout()); //paneau pour la barre recherche et la liste de membres dans la base de donnée
		
		JPanel barreRecherche = new JPanel(new FlowLayout()); //panneau pour la barre de recherche de membres
		JTextField text = new JTextField("Rechercher membre"); //champ de texte pour la recherche
		JButton image = new JButton(new ImageIcon(new ImageIcon("./res/loading.gif").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT))); //boutton qui lance la recherche
		
		/** Ajout des element de la barre de recherche dans le paneau **/
		barreRecherche.add(text); 
		barreRecherche.add(image);
		
		/** Liste des membres qui peut-etre rajouter dans un événement **/
		DefaultMutableTreeNode allUsersTree = new DefaultMutableTreeNode("Membres");
		List<Entity> allUsers = User.factory.getAll();
		for(int i = 0; i < allUsers.size(); i++) {
			User member = (User) allUsers.get(i);
			allUsersTree.add(new DefaultMutableTreeNode(member.getFirstName() + " " + member.getLastName()));
		}
		JTree listeMembres = new JTree(allUsersTree);
		JScrollPane liste = new JScrollPane(listeMembres); //ajout de la liste dans un JScroll pan pour avoir une barre de scroll
		
		/** Ajout et placement des élements de recheche dans le paneau **/
		search.add(barreRecherche, BorderLayout.NORTH);
		search.add(liste, BorderLayout.CENTER);
		
		/** Deuxième liste de la même manière contenant les participants à l'événement **/
		DefaultMutableTreeNode allparticipantsTree = new DefaultMutableTreeNode("Membres participants");
		allparticipantsTree.add(new DefaultMutableTreeNode("Rachid Ben mha dit 'La salope' "));
		JTree listeMembresParticipantsTree = new JTree(allparticipantsTree);
		JScrollPane listeMembresParticipants = new JScrollPane(listeMembresParticipantsTree);
		
		panMembres.add(search);
		panMembres.add(listeMembresParticipants);
		
		participants.add(labelParticipants);
		participants.add(panMembres);
		
		//panel degré d'importance de l'évenement
		JPanel degreeImportance = new JPanel(new GridLayout(1,2));
		JLabel labelImportance = new JLabel("Style d'événement : ");
		labelImportance.setPreferredSize(new Dimension(350, 30));
		Object[] elements = new Object[] {"", "RDV personnel déplaçable", "RDV proffessionel déplaçable", "RDV personnel non déplaçable", "RDV proffessionnel non déplaçable", "Autre"};
		JComboBox importance = new JComboBox(elements);
		degreeImportance.add(labelImportance);
		degreeImportance.add(importance);
		
		//CheckBox pour si les detailles de l'événement est visible ou non
		JPanel eventVisible = new JPanel(new GridLayout(1,2));
		JLabel labelVisible = new JLabel("Descrition de l'événement caché : ");
		labelVisible.setPreferredSize(new Dimension(400, 30));
		JCheckBox checkVisible = new JCheckBox();
		eventVisible.add(labelVisible);
		eventVisible.add(checkVisible);
		
		newEvent.add(name);
		newEvent.add(dateBegin);
		newEvent.add(dateEnd);
		newEvent.add(description);
		newEvent.add(participants);
		newEvent.add(degreeImportance);
		newEvent.add(eventVisible);
		
		
		//Panel bouton
		JPanel buttonPane = new JPanel(new GridLayout(1,2));
		JButton annuleEvent = new JButton("Annuler");
		JButton ajoutEvent = new JButton("Ajouter l'événement");
		buttonPane.add(annuleEvent);
		buttonPane.add(ajoutEvent);

	    this.setLocationRelativeTo(null);
	    
	    ajoutEvent.addActionListener(new CreateEventListener(this));
	    annuleEvent.addActionListener(new AnnuleEvent(this));
	    
	    this.add(newEvent, BorderLayout.CENTER);
	    this.add(buttonPane, BorderLayout.SOUTH);
	    this.setVisible(true);
	    this.addWindowListener(new CloseWindow(this));
	    image.addActionListener(new SearchUser());
	}

	public String getName()
	{
		return textName.getText();
	}
	
	public String getDesc()
	{
		return textDescription.getText();
	}
	
	public JCalendar getDateBegin()
	{
		return calendarBegin;
	}
	
	public JCalendar getDateEnd()
	{
		return calendarEnd;
	}
	
	public int getHourBegin()
	{
		return (Integer)hourBegin.getValue();
	}
	
	public int getMinuteBegin()
	{
		return (Integer)minuteBegin.getValue();
	}
	
	public int getHourEnd()
	{
		return (Integer)hourEnd.getValue();
	}
	
	public int getMinuteEnd()
	{
		return (Integer)minuteEnd.getValue();
	}
	
	@Override
	public void update(Observable arg0, Object state) {
/*//		switch((int) state) {
//			case success:
//				this.dispose();
//				break;
//				
//			case gange:
				status.setText("Cette date n'est pas libre");*/
		}
}