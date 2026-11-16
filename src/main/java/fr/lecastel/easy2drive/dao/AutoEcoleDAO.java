package fr.lecastel.easy2drive.dao;

import fr.lecastel.easy2drive.modele.Adresse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AutoEcoleDAO {
    public void modifierAdresse(Connection connexion, int idEcole, int idDirecteur, Adresse adresse)
            throws SQLException {
        String sql = """
                UPDATE AutoEcole SET ville = ?, codePostal = ?, rue = ?
                WHERE id = ? AND EXISTS (
                    SELECT 1 FROM Utilisateur u JOIN Directeur d ON d.idUtilisateur = u.id
                    WHERE u.id = ? AND u.idAutoEcole = AutoEcole.id
                )
                """;
        try (PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, adresse.getVille());
            requete.setString(2, adresse.getCodePostal());
            requete.setString(3, adresse.getRue());
            requete.setInt(4, idEcole);
            requete.setInt(5, idDirecteur);
            if (requete.executeUpdate() != 1) {
                throw new SQLException("Auto-école introuvable ou modification non autorisée.");
            }
        }
    }
}
