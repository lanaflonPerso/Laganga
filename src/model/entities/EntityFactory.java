package model.entities;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Database;
import model.FieldsList;

/**
 * Un ModelFactory est associé à une table de la base de données et à une classe du modèle (ex: users/User).
 * Il permet d'effectuer plusieurs opérations sur le modèle, comme récupérer un certain nombre de lignes depuis la
 * base de données, instancier la classe qui leur est associée et créer de nouvelles entrées.
 * 
 * @author Julien Valverdé
 */
public class EntityFactory {
	// Liste des objets instanciés
	private Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	
	/*
	 * Attributs
	 */
	private String className;
	private Class<?> classObject; // Classe de l'entité
	
	private String table; // Nom de la table associée à l'entité
	private String single; // Appellation d'une entité seule
	private String prefix; // Préfixe des champs
	private EntityFactory parent; // Entité parente
	private FieldsList fieldsList; // Liste des champs
	
	private Map<String, EntityFactory> joinedEntities = new HashMap<String, EntityFactory>(); // Entités jointes (<Nom du champ, Usine de l'entité à joindre>)
	
	/*
	 * Getteurs
	 */
	public String getClassName() {
		return className;
	}
	public Class<?> getClassObject() {
		return classObject;
	}
	public String getTable() {
		return table;
	}
	public String getSingle() {
		return single;
	}
	public String getPrefix() {
		return prefix;
	}
	public EntityFactory getParent() {
		return parent;
	}
	public FieldsList getFieldsList() {
		return fieldsList;
	}
	public Map<String, EntityFactory> getJoinedEntities() {
		return joinedEntities;
	}
	
	/*
	 * Setteurs
	 */
	public void setClassName(String className) {
		try {
			classObject = Class.forName(className);
			this.className = className;
		}
		catch (ClassNotFoundException e) {
			System.err.println("Impossible d'initialiser l'usine de "+className+" : "+e);
		}
	}
	public void setTable(String table) {
		this.table = table;
	}
	public void setSingle(String single) {
		this.single = single;
		this.prefix = single+"_";
	}
	public void setParent(EntityFactory parent) {
		this.parent = parent;
	}
	public void setFieldsList(FieldsList fields) {
		fieldsList = new FieldsList();
		
		// L'entité a un parent
		if (parent != null)
			fieldsList.addAll(parent.getFieldsList());
		
		fieldsList.addAll(fields);
	}
	public void setJoinedEntities(Map<String, EntityFactory> joinedEntities) {
		this.joinedEntities = joinedEntities;
	}
	
	// Création d'une nouvelle entité instanciée
	private Entity newEntity(ResultSet res, String tableAlias) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException, Exception {
		Entity entity = (Entity) classObject.getConstructor().newInstance();
		entities.put(res.getInt(tableAlias+"."+getPrefix()+"id"), entity);
		
		entity.setFactory(this);
		entity.save(res, tableAlias);
		
		return entity;
	}
	
	/**
	 * Assemble une requête SQL pour récupérer des objets.
	 * 
	 * @param whereClause Clause WHERE de la requête (null pour ne pas la préciser).
	 * @param additionalClauses Clauses additionnelles (null pour ne pas en préciser).
	 * @return La requête SQL.
	 */
	private String getSelectQuery(String clauses) {
		String query = "SELECT * FROM `"+getTable()+"`";
		
		query += "\n"+getJoints();
		
		if (clauses == null)
			clauses = "ORDER BY `"+getPrefix()+"id` DESC";
		
		return query+"\n"+clauses;
	}
	
	/*
	 * getInstanceFromID
	 * Renvoie l'instance du modèle d'ID id. Si une telle instance n'existe pas, elle est crée.
	 */
	public Entity getFromResultSet(ResultSet res, String tableAlias) throws Exception {
		Entity entity = entities.get(res.getInt(tableAlias+"."+getPrefix()+"id"));
		
		if (entity == null)
			entity = newEntity(res, tableAlias);
		
		return entity;
	}
	public Entity getFromResultSet(ResultSet res) throws Exception {
		return getFromResultSet(res, getTable());
	}
	
	/*
	 * get
	 * 	clause: Clause WHERE de la requête
	 * 
	 * Retourne les instances du modèle vérifiant la requête passée.
	 */
	private List<Entity> get(ResultSet res) throws SQLException, Exception {
		List<Entity> list = new ArrayList<Entity>();
		
		// ID de la dernière ligne traitée
		int previousId = -1;
		
		// Parcours des lignes récupérées
		while (res.next()) {
			Entity ent = getFromResultSet(res); // Récupération/création de l'entité
			
			// On traite une nouvelle entité, l'enregistrer dans la liste
			if (ent.getID() != previousId) {
				list.add(ent);
				previousId = ent.getID();
			}
		}
		
		return list;
	}
	public List<Entity> get(String query) throws SQLException, Exception {
		return get(Database.database.prepareAndExecute(getSelectQuery(query)));
	}
	public List<Entity> get(String query, FieldsList fields, Map<String, Object> values) throws SQLException, Exception {
		return get(Database.database.prepareAndExecute(getSelectQuery(query), fields, values));
	}
	
	/*
	 * getAll
	 * Retourne une liste de tous les objets du modèle.
	 */
	public List<Entity> getAll() throws Exception {
		return get((String) null);
	}
	
	public Entity getSingle(String query, FieldsList fields, Map<String, Object> values) throws SQLException, Exception {
		List<Entity> list = get(query, fields, values);
		
		if (list.size() == 0)
			return null;
		return list.get(0);
	}
	public Entity getSingle(String query) throws SQLException, Exception {
		List<Entity> list = get(query);
		
		if (list.size() == 0)
			return null;
		return list.get(0);
	}
	
	private static FieldsList getByIDQueryFields = new FieldsList();
	static {
		getByIDQueryFields.add("id", "int");
	}
	
	/*
	 * getByID
	 * 	id : ID de la ligne du modèle à retourner
	 * 
	 * Retourne l'objet du modèle correspondant à l'ID indiqué.
	 */
	public Entity getByID(int id) throws Exception {
		// Valeurs
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("id", id);
		
		return getSingle("WHERE `"+getPrefix()+"id` = ?", getByIDQueryFields, values);
	}
	
	
	/*
	 * Insertion
	 */
	
	public String getInsertQuery(FieldsList fields) {
		String query = "INSERT INTO `"+table+"`("+fields.toInsertQueryString(prefix)+") VALUES(";
		
		// Liste des valeurs à binder
		for (int i = 0; i < fields.size(); i++)
			query += "?, ";
		
		return query.substring(0, query.length() - 2)+")";
	}
	
	// Insertion d'une nouvelle entité
	public Entity insert(Map<String, Object> values) throws SQLException, Exception {
		FieldsList fields = fieldsList.clone();
		fields.remove("id"); // On ne veut pas insérer l'ID manuellement, c'est la BDD qui le choisit
		
		ResultSet res = Database.database.prepareUpdateAndExecute(getInsertQuery(fields), fields, values);
		
		res.next();
		return getByID(res.getInt(1)); // Renvoi de l'entité nouvellement crée
	}
	
	
	/*
	 * Construction des requêtes SQL
	 */
	
	// Joindre récursivement toutes les entités
	public String getJoints(String tableAlias) {
		String query = "";
		
		// Parcourir les champs de l'entité
		for (int i = 0; i < getFieldsList().size(); i++) {
			String field = getFieldsList().getName(i);
			EntityFactory joined = getJoinedEntities().get(field);
			
			// Ce champ comprend une jointure
			if (joined != null) {
				String joinedTableAlias = joined.getTable()+"_"+field;
				query += getJoint(tableAlias, joined, joinedTableAlias, field)+"\n"+joined.getJoints(joinedTableAlias);
			}
		}
		
		return query;
	}
	public String getJoints() {
		return getJoints(getTable());
	}
	
	// Clause de jointure d'une entité
	public String getJoint(String tableAlias, EntityFactory joined, String joinedTableAlias, String onField) {
		return "JOIN `"+joined.getTable()+"` AS `"+joinedTableAlias+"` ON `"+joinedTableAlias+"`.`"+joined.getPrefix()+"id` = `"+tableAlias+"`.`"+getPrefix()+onField+"`";
	}
	
	// Update
	public String getUpdateQuery(FieldsList fields) {
		return
				"UPDATE `"+table+"`\n"+
				"SET "+fields.toUpdateQueryString(getPrefix())+"\n"+
				"WHERE `"+prefix+"id` = ?";
	}
	
	// Delete
	public String getDeleteQuery() {
		return "DELETE FROM `"+getTable()+"` WHERE `"+getPrefix()+"id` = ?";
	}
}