package model.entities;

import java.util.Calendar;

import model.FieldsList;

/**
 * Une Entity est une entrée de table.
 * 
 * @author Julien Valverdé
 */
public class User extends Entity {
	/*
	 * Objet usine
	 */
	public static EntityFactory factory = new EntityFactory();
	static {
		factory.setClassName("model.entities.User");
		factory.setTable("users");
		factory.setSingle("user");
		factory.setParent(Entity.factory);
		
		// Champs
		FieldsList fields = new FieldsList();
		fields.add("firstName", "String");
		fields.add("lastName", "String");
		fields.add("email", "String");
		fields.add("password", "String");
		fields.add("birth", "Date");
		
		factory.setFieldsList(fields);
	}
	
	// Getteurs
	public String getFirstName() {
		return (String) getFieldsValues().get("firstName");
	}
	public String getLastName() {
		return (String) getFieldsValues().get("lastName");
	}
	public String getEmail() {
		return (String) getFieldsValues().get("email");
	}
	public String getPassword() {
		return (String) getFieldsValues().get("password");
	}
	public Calendar getBirth() {
		return (Calendar) getFieldsValues().get("birth");
	}
	
	// Setteurs
	public void setFirstName(String firstName) {
		getFieldsValues().put("firstName", firstName);
	}
	public void setLastName(String lastName) {
		getFieldsValues().put("lastName", lastName);
	}
	public void setEmail(String email) {
		getFieldsValues().put("email", email);
	}
	public void setPassword(String password) {
		getFieldsValues().put("password", password);
	}
	public void setBirth(Calendar birth) {
		getFieldsValues().put("birth", birth);
	}
}