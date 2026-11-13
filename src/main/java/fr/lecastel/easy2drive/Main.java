package fr.lecastel.easy2drive;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, 
                "Bienvenue sur Easy2Drive Gestion (Mission 3) !\nL'environnement Java est prêt.",
                "Easy2Drive Gestion", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
