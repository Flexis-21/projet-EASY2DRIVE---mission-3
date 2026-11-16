package fr.lecastel.easy2drive.vue;

import fr.lecastel.easy2drive.controleur.CompteControleur;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {
    private final CompteControleur controleur;
    private final JTextField login = new JTextField(23);
    private final JPasswordField motDePasse = new JPasswordField(23);

    public LoginFrame(CompteControleur controleur) {
        super("Connexion - Easy2Drive");
        this.controleur = controleur;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Formulaire formulaire = new Formulaire();
        formulaire.ajouterTitre("Connexion à Easy2Drive");
        formulaire.ajouterLigne("Identifiant", login);
        formulaire.ajouterLigne("Mot de passe", motDePasse);

        JButton connexion = new JButton("Se connecter");
        connexion.addActionListener(evenement -> connecter());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(connexion);

        JPanel blocConnexion = new JPanel(new BorderLayout(0, 8));
        blocConnexion.add(formulaire, BorderLayout.CENTER);
        blocConnexion.add(actions, BorderLayout.SOUTH);

        JPanel contenu = new JPanel(new BorderLayout(22, 0));
        contenu.setBorder(BorderFactory.createEmptyBorder(16, 24, 14, 24));
        URL illustration = getClass().getResource("/images/autoecole.png");
        if (illustration != null) {
            ImageIcon photo = new ImageIcon(illustration);
            Image miniature = photo.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
            contenu.add(new JLabel(new ImageIcon(miniature)), BorderLayout.WEST);
        }
        contenu.add(blocConnexion, BorderLayout.CENTER);
        setContentPane(contenu);
        getRootPane().setDefaultButton(connexion);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void connecter() {
        char[] saisie = motDePasse.getPassword();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            if (controleur.seConnecter(login.getText(), saisie)) {
                AccueilFrame accueil = new AccueilFrame(controleur);
                dispose();
                accueil.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants incorrects.",
                        "Connexion", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Connexion impossible. Vérifie que la base Easy2Drive est"
                    + "\naccessible et que config/db.properties est correctement renseigné.",
                    "Connexion", JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(saisie, '\0');
            motDePasse.setText("");
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
