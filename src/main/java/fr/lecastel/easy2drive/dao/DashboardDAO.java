

package fr.lecastel.easy2drive.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DashboardDAO {


   private final Connection connexion;


   public DashboardDAO(Connection connexion) {
       this.connexion = connexion;
   }


   // =====================================
   // 1. LEÇONS PREVUES AUJOURD'HUI
   // =====================================


   public List<String> leconsDuJour(int idAutoEcole)
           throws SQLException {


       List<String> resultat = new ArrayList<>();


       String sql = """
           SELECT p.plId,
                  p.plDateHeureDebut,
                  p.plDuree,
                  e.utNom AS nomEleve,
                  e.utPrenom AS prenomEleve,
                  f.utNom AS nomFormateur,
                  f.utPrenom AS prenomFormateur
           FROM Planning p
           JOIN Utilisateur e ON e.utId = p.plIdEleve
           JOIN Utilisateur f ON f.utId = p.plIdFormateur
           WHERE DATE(p.plDateHeureDebut) = CURRENT_DATE()
             AND p.plStatut = 'PREVUE'
             AND e.utIdAutoEcole = ?
             AND f.utIdAutoEcole = ?
           ORDER BY p.plDateHeureDebut
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setInt(1, idAutoEcole);
           ps.setInt(2, idAutoEcole);


           ResultSet rs = ps.executeQuery();


           while (rs.next()) {
               resultat.add(
                   rs.getTimestamp("plDateHeureDebut")
                   + " | Élève : "
                   + rs.getString("prenomEleve") + " "
                   + rs.getString("nomEleve")
                   + " | Formateur : "
                   + rs.getString("prenomFormateur") + " "
                   + rs.getString("nomFormateur")
                   + " | Durée : "
                   + rs.getInt("plDuree") + " min"
               );
           }
       }


       return resultat;
   }




   // =====================================
   // 2. PROCHAINES LEÇONS
   // =====================================


   public List<String> prochainesLecons(int idAutoEcole)
           throws SQLException {


       List<String> resultat = new ArrayList<>();


       String sql = """
           SELECT p.plDateHeureDebut,
                  p.plDuree,
                  e.utNom AS nomEleve,
                  e.utPrenom AS prenomEleve,
                  f.utNom AS nomFormateur,
                  f.utPrenom AS prenomFormateur
           FROM Planning p
           JOIN Utilisateur e ON e.utId = p.plIdEleve
           JOIN Utilisateur f ON f.utId = p.plIdFormateur
           WHERE p.plDateHeureDebut >= NOW()
             AND p.plStatut = 'PREVUE'
             AND e.utIdAutoEcole = ?
             AND f.utIdAutoEcole = ?
           ORDER BY p.plDateHeureDebut
           LIMIT 10
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setInt(1, idAutoEcole);
           ps.setInt(2, idAutoEcole);


           ResultSet rs = ps.executeQuery();


           while (rs.next()) {
               resultat.add(
                   rs.getTimestamp("plDateHeureDebut")
                   + " | Élève : "
                   + rs.getString("prenomEleve") + " "
                   + rs.getString("nomEleve")
                   + " | Formateur : "
                   + rs.getString("prenomFormateur") + " "
                   + rs.getString("nomFormateur")
               );
           }
       }


       return resultat;
   }




   // =====================================
   // 3. ELEVES SANS FORMATEUR REFERENT
   // =====================================


   public List<String> elevesSansFormateur(int idAutoEcole)
           throws SQLException {


       List<String> resultat = new ArrayList<>();


       String sql = """
           SELECT u.utId, u.utNom, u.utPrenom
           FROM Eleve e
           JOIN Utilisateur u ON u.utId = e.elIdUtilisateur
           WHERE e.elIdFormaEleve IS NULL
             AND e.elEstArchive = 0
             AND u.utIdAutoEcole = ?
           ORDER BY u.utNom
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setInt(1, idAutoEcole);


           ResultSet rs = ps.executeQuery();


           while (rs.next()) {
               resultat.add(
                   rs.getInt("utId") + " - "
                   + rs.getString("utPrenom") + " "
                   + rs.getString("utNom")
               );
           }
       }


       return resultat;
   }




   // =====================================
   // 4. ELEVES SANS LEÇON DEPUIS 30 JOURS
   // =====================================


   public List<String> elevesInactifs(int idAutoEcole)
           throws SQLException {


       List<String> resultat = new ArrayList<>();


       String sql = """
           SELECT u.utId,
                  u.utNom,
                  u.utPrenom,
                  MAX(p.plDateHeureDebut) AS derniereLecon
           FROM Eleve e
           JOIN Utilisateur u ON u.utId = e.elIdUtilisateur
           LEFT JOIN Planning p
               ON p.plIdEleve = e.elIdUtilisateur
               AND p.plStatut = 'PREVUE'
               AND p.plDateHeureDebut < NOW()
           WHERE e.elEstArchive = 0
             AND u.utIdAutoEcole = ?
           GROUP BY u.utId, u.utNom, u.utPrenom
           HAVING derniereLecon IS NULL
               OR derniereLecon <= DATE_SUB(NOW(), INTERVAL 30 DAY)
           ORDER BY derniereLecon
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setInt(1, idAutoEcole);


           ResultSet rs = ps.executeQuery();


           while (rs.next()) {


               String derniere = rs.getTimestamp("derniereLecon") == null
                   ? "Aucune leçon enregistrée"
                   : rs.getTimestamp("derniereLecon").toString();


               resultat.add(
                   rs.getString("utPrenom") + " "
                   + rs.getString("utNom")
                   + " | Dernière leçon : " + derniere
               );
           }
       }


       return resultat;
   }
}
