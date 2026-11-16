package fr.lecastel.easy2drive;

import fr.lecastel.easy2drive.controleur.CompteControleur;
import fr.lecastel.easy2drive.dao.DatabaseConnection;
import fr.lecastel.easy2drive.vue.LoginFrame;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::ouvrirApplication);
    }

    private static void ouvrirApplication() {
        Path configuration = Path.of(System.getProperty("easy2drive.config", "config/db.properties"));
        try {
            DatabaseConnection base = new DatabaseConnection(configuration);
            new LoginFrame(new CompteControleur(base)).setVisible(true);
        } catch (NoSuchFileException exception) {
            JOptionPane.showMessageDialog(null,
                    "Copie config/db.properties.example vers config/db.properties,\n"
                    + "puis renseigne la connexion à la base de données.\n\n"
                    + "Fichier attendu : " + configuration.toAbsolutePath(),
                    "Configuration Easy2Drive", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(null, "Vérifie le fichier de configuration :\n"
                    + configuration.toAbsolutePath() + "\n\n" + exception.getMessage(),
                    "Configuration Easy2Drive", JOptionPane.ERROR_MESSAGE);
        }
    }
}
