package fr.lecastel.easy2drive.vue;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import fr.lecastel.easy2drive.controleur.CompteControleur;
import fr.lecastel.easy2drive.modele.Utilisateur;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AccueilFrame extends JFrame {
    private final CompteControleur controleur;
    private final JPanel contenu = new JPanel(new BorderLayout());

    public AccueilFrame(CompteControleur controleur) {
        super("Easy2Drive");
        this.controleur = controleur;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Utilisateur utilisateur = controleur.getUtilisateur();
        JPanel entete = new JPanel(new BorderLayout());
        entete.setBackground(new Color(51, 51, 51));
        JLabel titre = new JLabel("  Easy2Drive  |  " + utilisateur.getAutoEcole().getRaisonSociale());
        titre.setForeground(Color.WHITE);
        titre.setFont(titre.getFont().deriveFont(Font.BOLD, 17f));
        titre.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
        entete.add(titre, BorderLayout.NORTH);

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        ajouterBouton(menu, "Accueil", this::afficherAccueil);
        ajouterBouton(menu, "Mon profil", this::afficherProfil);
        if (utilisateur.peutModifierProfil()) {
            ajouterBouton(menu, "Gestion du compte", this::afficherGestionCompte);
        }
        ajouterBouton(menu, "Mon auto-école", this::afficherEcole);
        ajouterBouton(menu, "Déconnexion", this::deconnecter);
        entete.add(menu, BorderLayout.CENTER);

        contenu.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        add(entete, BorderLayout.NORTH);
        add(contenu, BorderLayout.CENTER);
        afficherAccueil();
        setMinimumSize(new Dimension(680, 445));
        setSize(735, 465);
        setLocationRelativeTo(null);
    }

    private void ajouterBouton(JPanel menu, String libelle, Runnable action) {
        JButton bouton = new JButton(libelle);
        bouton.addActionListener(evenement -> action.run());
        menu.add(bouton);
    }

    private void afficherAccueil() {
        Utilisateur utilisateur = controleur.getUtilisateur();
        Formulaire accueil = new Formulaire();
        accueil.ajouterTitre("Bonjour, " + utilisateur.getIdentite() + " !");
        accueil.ajouterValeur("Rôle", utilisateur.getRole());
        accueil.ajouterValeur("Auto-école", utilisateur.getAutoEcole().getRaisonSociale());
        JPanel page = new JPanel(new BorderLayout());
        page.add(accueil, BorderLayout.NORTH);
        if (controleur.peutConsulterTableauDeBord()) {
        page.add(creerTableauDeBord(), BorderLayout.CENTER);
    }
        JLabel pied = new JLabel("© 2025 Easy2Drive", SwingConstants.CENTER);
        page.add(pied, BorderLayout.SOUTH);
        afficher(page);
    }
    
    private JPanel creerTableauDeBord() {
    JPanel panneau = new JPanel(new BorderLayout());

    try {
        var donnees = controleur.chargerTableauDeBord();
        var onglets = new JTabbedPane();
        String[] colonnesLecon = {"Date et heure", "Élève", "Formateur"};
        String[] colonnesEleve = {"Prénom", "Nom"};

        onglets.addTab("Leçons du jour", creerTable(
                donnees.leconsDuJour(), colonnesLecon, "Aucune leçon prévue aujourd’hui."));
        onglets.addTab("Prochaines leçons", creerTable(
                donnees.prochainesLecons(), colonnesLecon, "Aucune leçon prévue à partir de demain."));
        onglets.addTab("Sans référent", creerTable(
                donnees.sansReferent(), colonnesEleve, "Tous les élèves ont un référent."));
        onglets.addTab("Sans leçon depuis 30 jours", creerTable(
                donnees.sansLeconRecente(), colonnesEleve, "Aucun élève à signaler."));
        onglets.addTab("Chevauchements", creerTable(
                donnees.chevauchements(), colonnesLecon, "Aucun chevauchement détecté."));

        panneau.add(onglets, BorderLayout.CENTER);
    } catch (SQLException exception) {
        panneau.add(new JLabel("Impossible de charger le tableau de bord."));
        exception.printStackTrace();
    }

    return panneau;
}

private JPanel creerTable(List<String[]> lignes, String[] colonnes, String messageVide) {
    JPanel panneau = new JPanel(new BorderLayout());

    if (lignes.isEmpty()) {
        panneau.add(new JLabel(messageVide, SwingConstants.CENTER));
        return panneau;
    }

    JTable tableau = new JTable(lignes.toArray(String[][]::new), colonnes) {
        @Override
        public boolean isCellEditable(int ligne, int colonne) {
            return false;
        }
    };

    panneau.add(new JScrollPane(tableau), BorderLayout.CENTER);
    return panneau;
}

    private void afficherProfil() {
        afficher(new ProfilPanel(controleur, this::afficherProfil));
    }

    private void afficherGestionCompte() {
        ProfilPanel profil = new ProfilPanel(controleur, this::afficherProfil);
        afficher(profil);
        profil.modifierProfil();
    }

    private void afficherEcole() {
        afficher(new EcolePanel(controleur, this::afficherEcole));
    }

    private void afficher(JPanel page) {
        contenu.removeAll();
        contenu.add(page, BorderLayout.CENTER);
        contenu.revalidate();
        contenu.repaint();
    }

    private void deconnecter() {
        controleur.seDeconnecter();
        dispose();
        new LoginFrame(controleur).setVisible(true);
    }
}
