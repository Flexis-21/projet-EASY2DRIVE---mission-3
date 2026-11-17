

package fr.lecastel.easy2drive.dao;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class PlanningDAO {


   private final Connection connexion;


   public PlanningDAO(Connection connexion) {
       this.connexion = connexion;
   }


   // =====================================
   // 1. CONSULTER LES LEÇONS D'UNE JOURNEE
   // =====================================


   public List<String> afficherLeconsJour(
           Date date, int idAutoEcole) throws SQLException {


       List<String> lecons = new ArrayList<>();


       String sql = """
           SELECT p.plId, p.plDateHeureDebut, p.plDuree,
                  e.utNom, e.utPrenom,
                  f.utNom, f.utPrenom
           FROM Planning p
           JOIN Eleve el ON el.elIdUtilisateur = p.plIdEleve
           JOIN Utilisateur e ON e.utId = el.elIdUtilisateur
           JOIN Formateur fo ON fo.foIdUtilisateur = p.plIdFormateur
           JOIN Utilisateur f ON f.utId = fo.foIdUtilisateur
           WHERE DATE(p.plDateHeureDebut) = ?
             AND p.plStatut = 'PREVUE'
             AND e.utIdAutoEcole = ?
             AND f.utIdAutoEcole = ?
           ORDER BY p.plDateHeureDebut
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setDate(1, date);
           ps.setInt(2, idAutoEcole);
           ps.setInt(3, idAutoEcole);


           ResultSet rs = ps.executeQuery();


           while (rs.next()) {
               lecons.add(
                   "Leçon #" + rs.getInt("plId")
                   + " | " + rs.getTimestamp("plDateHeureDebut")
                   + " | " + rs.getInt("plDuree") + " min"
                   + " | Élève : " + rs.getString("utNom")
               );
           }
       }


       return lecons;
   }




   // =====================================
   // 2. VERIFIER LES CONFLITS
   // =====================================


   private boolean conflitExiste(
           int idEleve,
           int idFormateur,
           LocalDateTime debut,
           int duree,
           int idAutoEcole,
           int idLeconIgnoree) throws SQLException {


       LocalDateTime fin = debut.plusMinutes(duree);


       String sql = """
           SELECT COUNT(*)
           FROM Planning p
           JOIN Utilisateur e ON e.utId = p.plIdEleve
           JOIN Utilisateur f ON f.utId = p.plIdFormateur
           WHERE p.plStatut = 'PREVUE'
             AND e.utIdAutoEcole = ?
             AND f.utIdAutoEcole = ?
             AND p.plId <> ?
             AND p.plDateHeureDebut < ?
             AND DATE_ADD(
                   p.plDateHeureDebut,
                   INTERVAL p.plDuree MINUTE
                 ) > ?
             AND (
                   p.plIdEleve = ?
                   OR p.plIdFormateur = ?
                 )
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setInt(1, idAutoEcole);
           ps.setInt(2, idAutoEcole);
           ps.setInt(3, idLeconIgnoree);
           ps.setTimestamp(4, Timestamp.valueOf(fin));
           ps.setTimestamp(5, Timestamp.valueOf(debut));
           ps.setInt(6, idEleve);
           ps.setInt(7, idFormateur);


           ResultSet rs = ps.executeQuery();


           if (rs.next()) {
               return rs.getInt(1) > 0;
           }
       }


       return false;
   }




   // =====================================
   // 3. CREER UNE LEÇON
   // =====================================


   public boolean ajouterLecon(
           int idEleve,
           int idFormateur,
           LocalDateTime debut,
           int duree,
           int idAutoEcole) throws SQLException {


       if (duree <= 0) {
           throw new IllegalArgumentException(
               "La durée doit être positive."
           );
       }


       if (!memeAutoEcole(idEleve, idFormateur, idAutoEcole)) {
           throw new IllegalArgumentException(
               "L'élève et le formateur doivent appartenir à votre auto-école."
           );
       }


       if (conflitExiste(idEleve, idFormateur,
               debut, duree, idAutoEcole, -1)) {


           System.out.println("Conflit : élève ou formateur indisponible.");
           return false;
       }


       String sql = """
           INSERT INTO Planning
           (plIdEleve, plIdFormateur, plDuree,
            plDateHeureDebut, plStatut)
           VALUES (?, ?, ?, ?, 'PREVUE')
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setInt(1, idEleve);
           ps.setInt(2, idFormateur);
           ps.setInt(3, duree);
           ps.setTimestamp(4, Timestamp.valueOf(debut));


           return ps.executeUpdate() > 0;
       }
   }




   // =====================================
   // 4. MODIFIER OU DEPLACER UNE LEÇON
   // =====================================


   public boolean modifierLecon(
           int idLecon,
           int idEleve,
           int idFormateur,
           LocalDateTime debut,
           int duree,
           int idAutoEcole) throws SQLException {


       if (duree <= 0) {
           throw new IllegalArgumentException("Durée invalide.");
       }


       if (!memeAutoEcole(idEleve, idFormateur, idAutoEcole)) {
           throw new IllegalArgumentException("Auto-école invalide.");
       }


       if (!leconAppartientAutoEcole(idLecon, idAutoEcole)) {
           throw new IllegalArgumentException("Leçon introuvable.");
       }


       if (conflitExiste(idEleve, idFormateur,
               debut, duree, idAutoEcole, idLecon)) {
           return false;
       }


       String sql = """
           UPDATE Planning
           SET plIdEleve = ?,
               plIdFormateur = ?,
               plDateHeureDebut = ?,
               plDuree = ?
           WHERE plId = ?
             AND plStatut = 'PREVUE'
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setInt(1, idEleve);
           ps.setInt(2, idFormateur);
           ps.setTimestamp(3, Timestamp.valueOf(debut));
           ps.setInt(4, duree);
           ps.setInt(5, idLecon);


           return ps.executeUpdate() > 0;
       }
   }




   // =====================================
   // 5. ANNULER UNE LEÇON
   // =====================================


   public boolean annulerLecon(
           int idLecon, int idAutoEcole) throws SQLException {


       String sql = """
           UPDATE Planning p
           JOIN Utilisateur e ON e.utId = p.plIdEleve
           JOIN Utilisateur f ON f.utId = p.plIdFormateur
           SET p.plStatut = 'ANNULEE'
           WHERE p.plId = ?
             AND p.plStatut = 'PREVUE'
             AND e.utIdAutoEcole = ?
             AND f.utIdAutoEcole = ?
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setInt(1, idLecon);
           ps.setInt(2, idAutoEcole);
           ps.setInt(3, idAutoEcole);


           return ps.executeUpdate() > 0;
       }
   }




   // =====================================
   // 6. VERIFIER L'AUTO-ECOLE
   // =====================================


   private boolean memeAutoEcole(
           int idEleve, int idFormateur, int idAutoEcole)
           throws SQLException {


       String sql = """
           SELECT COUNT(*)
           FROM Eleve e
           JOIN Utilisateur ue ON ue.utId = e.elIdUtilisateur
           JOIN Formateur f ON f.foIdUtilisateur = ?
           JOIN Utilisateur uf ON uf.utId = f.foIdUtilisateur
           WHERE e.elIdUtilisateur = ?
             AND ue.utIdAutoEcole = ?
             AND uf.utIdAutoEcole = ?
             AND e.elEstArchive = 0
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setInt(1, idFormateur);
           ps.setInt(2, idEleve);
           ps.setInt(3, idAutoEcole);
           ps.setInt(4, idAutoEcole);


           ResultSet rs = ps.executeQuery();


           return rs.next() && rs.getInt(1) == 1;
       }
   }




   private boolean leconAppartientAutoEcole(
           int idLecon, int idAutoEcole) throws SQLException {


       String sql = """
           SELECT COUNT(*)
           FROM Planning p
           JOIN Utilisateur e ON e.utId = p.plIdEleve
           JOIN Utilisateur f ON f.utId = p.plIdFormateur
           WHERE p.plId = ?
             AND e.utIdAutoEcole = ?
             AND f.utIdAutoEcole = ?
       """;


       try (PreparedStatement ps = connexion.prepareStatement(sql)) {


           ps.setInt(1, idLecon);
           ps.setInt(2, idAutoEcole);
           ps.setInt(3, idAutoEcole);


           ResultSet rs = ps.executeQuery();


           return rs.next() && rs.getInt(1) == 1;
       }
   }
}
