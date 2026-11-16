package fr.lecastel.easy2drive.vue;

import fr.lecastel.easy2drive.controleur.CompteControleur;
import fr.lecastel.easy2drive.modele.Adresse;
import fr.lecastel.easy2drive.modele.AutoEcole;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EcolePanel extends JPanel {
    private final CompteControleur controleur;
    private final Runnable rafraichir;

    public EcolePanel(CompteControleur controleur, Runnable rafraichir) {
        super(new BorderLayout(0, 8));
        this.controleur = controleur;
        this.rafraichir = rafraichir;
        AutoEcole ecole = controleur.getUtilisateur().getAutoEcole();

        Formulaire fiche = new Formulaire();
        fiche.ajouterTitre("Mon auto-école");
        fiche.ajouterValeur("Auto-école", ecole.getRaisonSociale());
        fiche.ajouterAdresse(ecole.getAdresse());
        add(fiche, BorderLayout.NORTH);

        if (controleur.getUtilisateur().estDirecteur()) {
            JButton modifier = new JButton("Modifier les coordonnées");
            modifier.addActionListener(evenement -> modifierCoordonnees());
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
            actions.add(modifier);
            add(actions, BorderLayout.SOUTH);
        }
    }

    private void modifierCoordonnees() {
        Adresse adresse = controleur.getUtilisateur().getAutoEcole().getAdresse();
        Formulaire formulaire = new Formulaire();
        JTextField ville = formulaire.ajouterChamp("Ville", adresse.getVille());
        JTextField codePostal = formulaire.ajouterChamp("Code postal", adresse.getCodePostal());
        JTextField rue = formulaire.ajouterChamp("Rue", adresse.getRue());

        while (JOptionPane.showConfirmDialog(this, formulaire, "Modifier les coordonnées",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            try {
                controleur.modifierAutoEcole(new Adresse(
                        ville.getText().strip(), codePostal.getText().strip(), rue.getText().strip()));
                rafraichir.run();
                return;
            } catch (IllegalArgumentException exception) {
                afficherErreur(exception.getMessage());
            } catch (SQLException exception) {
                afficherErreur("Impossible d'enregistrer les coordonnées. Vérifie la connexion et les droits.");
            }
        }
    }

    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Modifier les coordonnées", JOptionPane.ERROR_MESSAGE);
    }
}
