package fr.lecastel.easy2drive.controleur;
import fr.lecastel.easy2drive.dao.TableauDeBordDAO;

import fr.lecastel.easy2drive.dao.AutoEcoleDAO;
import fr.lecastel.easy2drive.dao.CompteDAO;
import fr.lecastel.easy2drive.dao.DatabaseConnection;
import fr.lecastel.easy2drive.dao.EleveDAO;
import fr.lecastel.easy2drive.dao.UtilisateurDAO;
import fr.lecastel.easy2drive.modele.Adresse;
import fr.lecastel.easy2drive.modele.Compte;
import fr.lecastel.easy2drive.modele.Utilisateur;
import fr.lecastel.easy2drive.securite.PBKDF2Hasher;
import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Pattern;


public class CompteControleur {
    private static final Pattern MINUSCULE = Pattern.compile("[a-z]");
    private static final Pattern MAJUSCULE = Pattern.compile("[A-Z]");
    private static final Pattern CHIFFRE = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL = Pattern.compile("[^A-Za-z0-9_]");
    private final DatabaseConnection base;
    private final CompteDAO comptes = new CompteDAO();
    private final UtilisateurDAO utilisateurs = new UtilisateurDAO();
    private final AutoEcoleDAO autoEcoles = new AutoEcoleDAO();
    private final EleveDAO eleves = new EleveDAO();

    private Utilisateur utilisateur;
    private int idCompte;

    public CompteControleur(DatabaseConnection base) {
        this.base = base;
    }

    public boolean seConnecter(String login, char[] motDePasse) throws SQLException {
        seDeconnecter();
        if (login.isBlank() || motDePasse.length == 0) {
            return false;
        }

        Compte compte;
        Utilisateur profil;
        try (Connection connexion = base.ouvrir()) {
            compte = comptes.chercherParLogin(connexion, login.strip());
            if (compte == null || !PBKDF2Hasher.verifier(motDePasse, compte.getHash(), compte.getSel())) {
                return false;
            }
            profil = utilisateurs.chercherParCompte(connexion, compte.getId());
        }
        if (profil == null) {
            return false;
        }
        utilisateur = profil;
        idCompte = compte.getId();
        return true;
    }

    public void seDeconnecter() {
        utilisateur = null;
        idCompte = 0;
    }

    public Utilisateur getUtilisateur() {
        if (utilisateur == null) {
            throw new IllegalStateException("Connecte-toi pour accéder à ton espace.");
        }
        return utilisateur;
    }

    public void modifierProfil(String email, Adresse adresse) throws SQLException {
        Utilisateur profil = getUtilisateur();
        if (!profil.peutModifierProfil()) {
            throw new IllegalArgumentException("Ce profil est en consultation uniquement.");
        }
        if (email.isBlank() || email.length() > 255) {
            throw new IllegalArgumentException("Renseigne l'adresse e-mail (255 caractères maximum).");
        }
        if (profil.estEleve() || profil.estDirecteur()) {
            validerAdresse(adresse);
        }

        // L'e-mail et l'adresse sont enregistrés ensemble.
        try (Connection connexion = base.ouvrir(profil.getRole())) {
            connexion.setAutoCommit(false);
            try {
                utilisateurs.modifierEmail(connexion, profil.getId(), email);
                if (profil.estEleve()) {
                    eleves.modifierAdresse(connexion, profil.getId(), adresse);
                } else if (profil.estDirecteur()) {
                    autoEcoles.modifierAdresse(connexion, profil.getAutoEcole().getId(), profil.getId(), adresse);
                }
                connexion.commit();
            } catch (SQLException exception) {
                try {
                    connexion.rollback();
                } catch (SQLException erreurAnnulation) {
                    exception.addSuppressed(erreurAnnulation);
                }
                throw exception;
            }
        }

        profil.setEmail(email);
        if (profil.estEleve()) {
            profil.setAdresseEleve(adresse);
        } else if (profil.estDirecteur()) {
            profil.getAutoEcole().setAdresse(adresse);
        }
    }

    public boolean peutConsulterTableauDeBord() {
    Utilisateur profil = getUtilisateur();
    return profil.estDirecteur() || "Secretaire".equals(profil.getRole());
}

public TableauDeBordDAO.Donnees chargerTableauDeBord() throws SQLException {
    if (!peutConsulterTableauDeBord()) {
        throw new IllegalStateException("Le tableau de bord est réservé au personnel administratif.");
    }

    Utilisateur profil = getUtilisateur();
    try (Connection connexion = base.ouvrir(profil.getRole())) {
        return new TableauDeBordDAO().charger(connexion, profil.getAutoEcole().getId());
    }
}

    public void modifierAutoEcole(Adresse adresse) throws SQLException {
        Utilisateur profil = getUtilisateur();
        if (!profil.estDirecteur()) {
            throw new IllegalArgumentException("La modification est réservée au directeur de l'auto-école.");
        }
        validerAdresse(adresse);
        try (Connection connexion = base.ouvrir(profil.getRole())) {
            autoEcoles.modifierAdresse(connexion, profil.getAutoEcole().getId(), profil.getId(), adresse);
        }
        profil.getAutoEcole().setAdresse(adresse);
    }

    public void modifierMotDePasse(char[] actuel, char[] nouveau, char[] confirmation) throws SQLException {
        Utilisateur profil = getUtilisateur();
        if (!Arrays.equals(nouveau, confirmation)) {
            throw new IllegalArgumentException("Les nouveaux mots de passe ne correspondent pas.");
        }
        if (!motDePasseValide(nouveau)) {
            throw new IllegalArgumentException(
                    "Le nouveau mot de passe doit contenir au moins 12 caractères,\n"
                    + "une majuscule, une minuscule, un chiffre et un caractère spécial.");
        }

        try (Connection connexion = base.ouvrir(profil.getRole())) {
            Compte compte = comptes.chercherParId(connexion, idCompte);
            if (compte == null || !PBKDF2Hasher.verifier(actuel, compte.getHash(), compte.getSel())) {
                throw new IllegalArgumentException("Le mot de passe actuel est incorrect.");
            }
            String sel = PBKDF2Hasher.nouveauSel();
            String hash = PBKDF2Hasher.hacher(nouveau, sel);
            if (!comptes.modifierMotDePasse(connexion, compte, hash, sel)) {
                throw new IllegalArgumentException("Le mot de passe a changé entre-temps. Réessaie.");
            }
        }
    }

    private void validerAdresse(Adresse adresse) {
        if (adresse == null || adresse.getVille().isBlank()
                || adresse.getCodePostal().isBlank() || adresse.getRue().isBlank()) {
            throw new IllegalArgumentException("Renseigne la ville, le code postal et la rue.");
        }
        if (adresse.getVille().length() > 100 || adresse.getCodePostal().length() > 10
                || adresse.getRue().length() > 255) {
            throw new IllegalArgumentException("Adresse trop longue (ville 100, code postal 10, rue 255).");
        }
    }

    private boolean motDePasseValide(char[] motDePasse) {
        CharBuffer texte = CharBuffer.wrap(motDePasse);
        return Character.codePointCount(texte, 0, texte.length()) >= 12
                && MINUSCULE.matcher(texte).find()
                && MAJUSCULE.matcher(texte).find()
                && CHIFFRE.matcher(texte).find()
                && SPECIAL.matcher(texte).find();
    }
}
