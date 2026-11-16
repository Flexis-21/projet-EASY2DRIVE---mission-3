package fr.lecastel.easy2drive.dao;

import fr.lecastel.easy2drive.modele.Compte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompteDAO {
    public Compte chercherParLogin(Connection connexion, String login) throws SQLException {
        String sql = "SELECT id, mdp_hash, mdp_sel FROM Compte WHERE login = ?";
        try (PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, login);
            return lireCompte(requete);
        }
    }

    public Compte chercherParId(Connection connexion, int id) throws SQLException {
        String sql = "SELECT id, mdp_hash, mdp_sel FROM Compte WHERE id = ?";
        try (PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, id);
            return lireCompte(requete);
        }
    }

    public boolean modifierMotDePasse(Connection connexion, Compte compte, String hash, String sel)
            throws SQLException {
        // On vérifie que le mot de passe n'a pas été modifié depuis sa lecture.
        String sql = "UPDATE Compte SET mdp_hash = ?, mdp_sel = ? WHERE id = ? AND mdp_hash = ? AND mdp_sel = ?";
        try (PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, hash);
            requete.setString(2, sel);
            requete.setInt(3, compte.getId());
            requete.setString(4, compte.getHash());
            requete.setString(5, compte.getSel());
            return requete.executeUpdate() == 1;
        }
    }

    private Compte lireCompte(PreparedStatement requete) throws SQLException {
        try (ResultSet resultat = requete.executeQuery()) {
            if (!resultat.next()) {
                return null;
            }
            Compte compte = new Compte(resultat.getInt("id"), resultat.getString("mdp_hash"),
                    resultat.getString("mdp_sel"));
            if (resultat.next()) {
                throw new SQLException("Plusieurs comptes ont le même identifiant.");
            }
            return compte;
        }
    }
}
