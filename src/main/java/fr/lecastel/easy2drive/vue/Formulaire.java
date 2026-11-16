package fr.lecastel.easy2drive.vue;

import fr.lecastel.easy2drive.modele.Adresse;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Formulaire extends JPanel {
    private int ligne;

    Formulaire() {
        super(new GridBagLayout());
    }

    void ajouterTitre(String texte) {
        JLabel titre = new JLabel(texte);
        titre.setFont(titre.getFont().deriveFont(Font.BOLD, 18f));
        GridBagConstraints position = position(0);
        position.gridwidth = 2;
        position.fill = GridBagConstraints.HORIZONTAL;
        position.insets = new Insets(12, 4, 8, 4);
        add(titre, position);
        ligne++;
    }

    void ajouterValeur(String libelle, String valeur) {
        JLabel texte = new JLabel(valeur);
        texte.putClientProperty("html.disable", Boolean.TRUE);
        ajouterLigne(libelle, texte);
    }

    void ajouterAdresse(Adresse adresse) {
        ajouterValeur("Ville", adresse.getVille());
        ajouterValeur("Code postal", adresse.getCodePostal());
        ajouterValeur("Rue", adresse.getRue());
    }

    JTextField ajouterChamp(String libelle, String valeur) {
        JTextField champ = new JTextField(valeur, 23);
        ajouterLigne(libelle, champ);
        return champ;
    }

    void ajouterLigne(String libelle, JComponent champ) {
        JLabel etiquette = new JLabel(libelle + " :");
        etiquette.setLabelFor(champ);
        add(etiquette, position(0));
        GridBagConstraints positionChamp = position(1);
        positionChamp.weightx = 1;
        positionChamp.fill = GridBagConstraints.HORIZONTAL;
        add(champ, positionChamp);
        ligne++;
    }

    private GridBagConstraints position(int colonne) {
        GridBagConstraints position = new GridBagConstraints();
        position.gridx = colonne;
        position.gridy = ligne;
        position.anchor = GridBagConstraints.LINE_START;
        position.insets = new Insets(4, 4, 4, 10);
        return position;
    }
}
