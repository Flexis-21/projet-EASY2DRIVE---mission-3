package fr.lecastel.easy2drive.dao;

import fr.lecastel.easy2drive.modele.Adresse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EleveDAO {
    public void modifierAdresse(Connection connexion, int idUtilisateur, Adresse adresse) throws SQLException {
        String sql = "UPDATE Eleve SET ville = ?, codePostal = ?, rue = ? WHERE idUtilisateur = ?";
        try (PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, adresse.getVille());
            requete.setString(2, adresse.getCodePostal());
            requete.setString(3, adresse.getRue());
            requete.setInt(4, idUtilisateur);
            if (requete.executeUpdate() != 1) {
                throw new SQLException("Élève introuvable.");
            }
        }
    }
}
