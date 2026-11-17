package fr.lecastel.easy2drive.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableauDeBordDAO {

    public record Donnees(
            List<String[]> leconsDuJour,
            List<String[]> prochainesLecons,
            List<String[]> sansReferent,
            List<String[]> sansLeconRecente,
            List<String[]> chevauchements) {
    }

    private static final String REQUETE_LECONS = """
            SELECT DATE_FORMAT(p.plDateHeureDebut, '%d/%m/%Y %H:%i'),
                   CONCAT(e.prenom, ' ', e.nom),
                   CONCAT(f.prenom, ' ', f.nom)
            FROM Planning p
            JOIN Utilisateur e ON e.id = p.plIdEleve
            JOIN Utilisateur f ON f.id = p.plIdFormateur
                              AND f.idAutoEcole = e.idAutoEcole
            WHERE e.idAutoEcole = ?
            """;

    private static final String REQUETE_ELEVES = """
            SELECT u.prenom, u.nom
            FROM Eleve e
            JOIN Utilisateur u ON u.id = e.idUtilisateur
            WHERE u.idAutoEcole = ?
            """;

    public Donnees charger(Connection connexion, int idAutoEcole) throws SQLException {
        List<String[]> leconsDuJour = lire(connexion, idAutoEcole, REQUETE_LECONS + """
                AND p.plDateHeureDebut >= CURRENT_DATE
                AND p.plDateHeureDebut < CURRENT_DATE + INTERVAL 1 DAY
                ORDER BY p.plDateHeureDebut
                """);

        List<String[]> prochainesLecons = lire(connexion, idAutoEcole, REQUETE_LECONS + """
                AND p.plDateHeureDebut >= CURRENT_DATE + INTERVAL 1 DAY
                ORDER BY p.plDateHeureDebut
                """);

        List<String[]> sansReferent = lire(connexion, idAutoEcole, REQUETE_ELEVES + """
                AND e.idFormaEleve IS NULL
                ORDER BY u.nom, u.prenom
                """);

        List<String[]> sansLeconRecente = lire(connexion, idAutoEcole, REQUETE_ELEVES + """
                AND e.dateInscription <= CURRENT_DATE - INTERVAL 30 DAY
                AND NOT EXISTS (
                    SELECT 1
                    FROM Planning p
                    WHERE p.plIdEleve = e.idUtilisateur
                      AND TIMESTAMPADD(MINUTE, p.plDuree, p.plDateHeureDebut)
                          > CURRENT_TIMESTAMP - INTERVAL 30 DAY
                      AND TIMESTAMPADD(MINUTE, p.plDuree, p.plDateHeureDebut)
                          <= CURRENT_TIMESTAMP
                )
                ORDER BY u.nom, u.prenom
                """);

        List<String[]> chevauchements = lire(connexion, idAutoEcole, REQUETE_LECONS + """
                AND p.plDateHeureDebut >= CURRENT_DATE
                AND EXISTS (
                    SELECT 1
                    FROM Planning autre
                    WHERE autre.plId <> p.plId
                      AND (autre.plIdEleve = p.plIdEleve
                           OR autre.plIdFormateur = p.plIdFormateur)
                      AND autre.plDateHeureDebut
                          < TIMESTAMPADD(MINUTE, p.plDuree, p.plDateHeureDebut)
                      AND p.plDateHeureDebut
                          < TIMESTAMPADD(MINUTE, autre.plDuree, autre.plDateHeureDebut)
                )
                ORDER BY p.plDateHeureDebut
                """);

        return new Donnees(leconsDuJour, prochainesLecons, sansReferent,
                sansLeconRecente, chevauchements);
    }

    private List<String[]> lire(Connection connexion, int idAutoEcole, String sql)
            throws SQLException {
        List<String[]> lignes = new ArrayList<>();

        try (var requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, idAutoEcole);

            try (var resultat = requete.executeQuery()) {
                int nombreColonnes = resultat.getMetaData().getColumnCount();

                while (resultat.next()) {
                    String[] ligne = new String[nombreColonnes];
                    for (int i = 0; i < nombreColonnes; i++) {
                        ligne[i] = resultat.getString(i + 1);
                    }
                    lignes.add(ligne);
                }
            }
        }

        return lignes;
    }
}