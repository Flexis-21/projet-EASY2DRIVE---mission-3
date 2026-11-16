package fr.lecastel.easy2drive.dao;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;

public class DatabaseConnection {
    private final Properties configuration = new Properties();
    private final String url;

    public DatabaseConnection(Path fichier) throws IOException {
        try (Reader lecteur = Files.newBufferedReader(fichier)) {
            configuration.load(lecteur);
        }

        String hote = lireObligatoire("db.host");
        String base = lireObligatoire("db.name");
        lireObligatoire("db.user");
        String port = configuration.getProperty("db.port", "3306").strip();
        if (!hote.matches("[A-Za-z0-9.-]+") || !base.matches("[A-Za-z0-9_]+")
                || !port.matches("[0-9]{1,5}") || Integer.parseInt(port) == 0
                || Integer.parseInt(port) > 65535) {
            throw new IOException("Vérifie db.host, db.port et db.name dans la configuration.");
        }
        url = "jdbc:mysql://" + hote + ":" + port + "/" + base;
    }

    public Connection ouvrir() throws SQLException {
        return ouvrir("");
    }

    public Connection ouvrir(String role) throws SQLException {
        Properties options = new Properties();
        String prefixe = "db." + role.toLowerCase(Locale.ROOT);
        String utilisateurRole = configuration.getProperty(prefixe + ".user", "").strip();
        if (utilisateurRole.isEmpty()) {
            options.setProperty("user", configuration.getProperty("db.user").strip());
            options.setProperty("password", configuration.getProperty("db.password", ""));
        } else {
            options.setProperty("user", utilisateurRole);
            options.setProperty("password", configuration.getProperty(prefixe + ".password", ""));
        }
        options.setProperty("sslMode", configuration.getProperty("db.sslMode", "PREFERRED"));
        options.setProperty("allowPublicKeyRetrieval",
                configuration.getProperty("db.allowPublicKeyRetrieval", "false"));
        options.setProperty("connectTimeout", "4000");
        options.setProperty("socketTimeout", "8000");
        return DriverManager.getConnection(url, options);
    }

    private String lireObligatoire(String cle) throws IOException {
        String valeur = configuration.getProperty(cle, "").strip();
        if (valeur.isEmpty()) {
            throw new IOException("Renseigne " + cle + " dans la configuration.");
        }
        return valeur;
    }
}
