package fr.lecastel.easy2drive.vue;

import fr.lecastel.easy2drive.controleur.CompteControleur;
import fr.lecastel.easy2drive.modele.Adresse;
import fr.lecastel.easy2drive.modele.Utilisateur;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ProfilPanel extends JPanel {
    private final CompteControleur controleur;
    private final Runnable rafraichir;

    public ProfilPanel(CompteControleur controleur, Runnable rafraichir) {
        super(new BorderLayout(0, 8));
        this.controleur = controleur;
        this.rafraichir = rafraichir;
        Utilisateur utilisateur = controleur.getUtilisateur();

        Formulaire fiche = new Formulaire();
        fiche.ajouterTitre("Mon profil");
        fiche.ajouterValeur("Nom", utilisateur.getNom());
        fiche.ajouterValeur("Prénom", utilisateur.getPrenom());
        fiche.ajouterValeur("E-mail", utilisateur.getEmail());
        fiche.ajouterValeur("Statut", utilisateur.getRole());
        fiche.ajouterTitre(utilisateur.estEleve() ? "Mes coordonnées" : "Adresse de l'auto-école");
        fiche.ajouterAdresse(utilisateur.getAdresse());
        add(fiche, BorderLayout.NORTH);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (utilisateur.peutModifierProfil()) {
            JButton modifierProfil = new JButton("Modifier mon profil");
            modifierProfil.addActionListener(evenement -> modifierProfil());
            actions.add(modifierProfil);
        }
        JButton modifierMotDePasse = new JButton("Modifier mon mot de passe");
        modifierMotDePasse.addActionListener(evenement -> MotDePasseDialog.ouvrir(this, controleur));
        actions.add(modifierMotDePasse);
        add(actions, BorderLayout.SOUTH);
    }

    void modifierProfil() {
        Utilisateur utilisateur = controleur.getUtilisateur();
        Formulaire formulaire = new Formulaire();
        JTextField email = formulaire.ajouterChamp("E-mail", utilisateur.getEmail());
        boolean modifierAdresse = utilisateur.estEleve() || utilisateur.estDirecteur();
        JTextField ville = null;
        JTextField codePostal = null;
        JTextField rue = null;
        if (modifierAdresse) {
            if (utilisateur.estDirecteur()) {
                formulaire.ajouterTitre("Adresse de l'auto-école");
            }
            Adresse adresse = utilisateur.getAdresse();
            ville = formulaire.ajouterChamp("Ville", adresse.getVille());
            codePostal = formulaire.ajouterChamp("Code postal", adresse.getCodePostal());
            rue = formulaire.ajouterChamp("Rue", adresse.getRue());
        }

        while (JOptionPane.showConfirmDialog(this, formulaire, "Modifier mon profil",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            Adresse adresse = modifierAdresse
                    ? new Adresse(ville.getText().strip(), codePostal.getText().strip(), rue.getText().strip())
                    : null;
            try {
                controleur.modifierProfil(email.getText().strip(), adresse);
                rafraichir.run();
                return;
            } catch (IllegalArgumentException exception) {
                afficherErreur(exception.getMessage());
            } catch (SQLException exception) {
                if (exception.getErrorCode() == 1062) {
                    afficherErreur("Cette adresse e-mail est déjà utilisée.");
                } else {
                    afficherErreur("Impossible d'enregistrer le profil. Vérifie la connexion et les droits.");
                }
            }
        }
    }

    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Modifier mon profil", JOptionPane.ERROR_MESSAGE);
    }
}
