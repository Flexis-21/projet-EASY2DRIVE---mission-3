package fr.lecastel.easy2drive.vue;

import fr.lecastel.easy2drive.controleur.CompteControleur;
import java.awt.Component;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class MotDePasseDialog {
    private static final String TITRE = "Modifier mon mot de passe";

    private MotDePasseDialog() {
    }

    public static void ouvrir(Component parent, CompteControleur controleur) {
        Formulaire formulaire = new Formulaire();
        JPasswordField actuel = new JPasswordField(23);
        JPasswordField nouveau = new JPasswordField(23);
        JPasswordField confirmation = new JPasswordField(23);
        formulaire.ajouterLigne("Mot de passe actuel", actuel);
        formulaire.ajouterLigne("Nouveau mot de passe", nouveau);
        formulaire.ajouterLigne("Confirmation", confirmation);

        while (JOptionPane.showConfirmDialog(parent, formulaire, TITRE,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            char[] saisieActuel = actuel.getPassword();
            char[] saisieNouveau = nouveau.getPassword();
            char[] saisieConfirmation = confirmation.getPassword();
            actuel.setText("");
            nouveau.setText("");
            confirmation.setText("");
            try {
                controleur.modifierMotDePasse(saisieActuel, saisieNouveau, saisieConfirmation);
                JOptionPane.showMessageDialog(parent, "Mot de passe modifié.", TITRE,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            } catch (IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(parent, exception.getMessage(), TITRE, JOptionPane.ERROR_MESSAGE);
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(parent, "Impossible d'enregistrer le mot de passe."
                        + " Vérifie la connexion et les droits.", TITRE, JOptionPane.ERROR_MESSAGE);
            } finally {
                Arrays.fill(saisieActuel, '\0');
                Arrays.fill(saisieNouveau, '\0');
                Arrays.fill(saisieConfirmation, '\0');
            }
        }
        actuel.setText("");
        nouveau.setText("");
        confirmation.setText("");
    }
}
