package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import model.FieldsList;
import model.LocalUser;
import model.entities.Group;
import view.WinCreateGroupe;

public class CreateGroupe implements ActionListener {

	private WinCreateGroupe ouient;

	public CreateGroupe(WinCreateGroupe ouient) {
		this.ouient = ouient;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	/*	Map<String, Object> values = new HashMap<String, Object>();
		values.put("name", champNomGroupe);
		values.put("owner", LocalUser.localUser.getUser());
		
		Group group = (Group) Group.factory.insert(values);
		group.addMember(LocalUser.localUser.getUser());*/
	}
}