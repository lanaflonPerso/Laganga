package controller;

import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import model.Database;
import model.LocalUser;
import model.entities.Notification;
import view.SplashWin;

public abstract class Main {
	public static void main(String[] args) throws Exception {
		// Désactivation de la décoration des fenêtres par Swing
		JFrame.setDefaultLookAndFeelDecorated(false);
		
		// Affichage du splash screen
		new SplashWin();
		
		// Initialisation de la connexion à la base de données
		TimeUnit.SECONDS.sleep(1);
		Database.database.connect();
		
		// Bypass de la connexion pour le debugging (connexion de l'utilisateur 1)
		//LocalUser.localUser.login("julien.valverde@netc.fr", "issou");
	}
}