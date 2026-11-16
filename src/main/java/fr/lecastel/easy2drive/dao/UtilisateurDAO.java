package fr.lecastel.easy2drive.dao;

import fr.lecastel.easy2drive.modele.Adresse;
import fr.lecastel.easy2drive.modele.AutoEcole;
import fr.lecastel.easy2drive.modele.Utilisateur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAO {
    public Utilisateur chercherParCompte(Connection connexion, int idCompte) throws SQLException {
        String sql = """
                SELECT u.id, u.nom, u.prenom, u.email,
                       a.id AS idAutoEcole, a.raisonSociale, a.ville, a.codePostal, a.rue,
                       e.ville AS villeEleve, e.codePostal AS codePostalEleve, e.rue AS rueEleve,
                       CASE WHEN d.idUtilisateur IS NOT NULL THEN 'Directeur'
                            WHEN f.idUtilisateur IS NOT NULL THEN 'Formateur'
                            WHEN e.idUtilisateur IS NOT NULL THEN 'Eleve'
                            ELSE 'Utilisateur'
                       END AS role
                FROM Utilisateur u
                JOIN AutoEcole a ON a.id = u.idAutoEcole
                LEFT JOIN Directeur d ON d.idUtilisateur = u.id
                LEFT JOIN Formateur f ON f.idUtilisateur = u.id
                LEFT JOIN Eleve e ON e.idUtilisateur = u.id
                WHERE u.idUser = ?
                """;
        try (PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, idCompte);
            try (ResultSet resultat = requete.executeQuery()) {
                if (!resultat.next()) {
                    return null;
                }
                Utilisateur utilisateur = lireUtilisateur(resultat);
                if (resultat.next()) {
                    throw new SQLException("Ce compte est rattaché à plusieurs profils.");
                }
                return utilisateur;
            }
        }
    }

    public void modifierEmail(Connection connexion, int idUtilisateur, String email) throws SQLException {
        String sql = "UPDATE Utilisateur SET email = ? WHERE id = ?";
        try (PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, email);
            requete.setInt(2, idUtilisateur);
            if (requete.executeUpdate() != 1) {
                throw new SQLException("Profil introuvable.");
            }
        }
    }

    private Utilisateur lireUtilisateur(ResultSet resultat) throws SQLException {
        Adresse adresseEcole = new Adresse(resultat.getString("ville"), resultat.getString("codePostal"),
                resultat.getString("rue"));
        AutoEcole autoEcole = new AutoEcole(resultat.getInt("idAutoEcole"),
                resultat.getString("raisonSociale"), adresseEcole);
        Adresse adresseEleve = new Adresse(resultat.getString("villeEleve"),
                resultat.getString("codePostalEleve"), resultat.getString("rueEleve"));
        return new Utilisateur(resultat.getInt("id"), resultat.getString("nom"),
                resultat.getString("prenom"), resultat.getString("email"), resultat.getString("role"),
                autoEcole, adresseEleve);
    }
}
