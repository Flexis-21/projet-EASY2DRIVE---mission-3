package fr.lecastel.easy2drive.modele;

public class Utilisateur {
    private final int id;
    private final String nom;
    private final String prenom;
    private final String role;
    private final AutoEcole autoEcole;
    private String email;
    private Adresse adresseEleve;

    public Utilisateur(int id, String nom, String prenom, String email, String role,
                       AutoEcole autoEcole, Adresse adresseEleve) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.autoEcole = autoEcole;
        this.adresseEleve = adresseEleve;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getIdentite() {
        return prenom + " " + nom;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AutoEcole getAutoEcole() {
        return autoEcole;
    }

    public Adresse getAdresse() {
        return estEleve() ? adresseEleve : autoEcole.getAdresse();
    }

    public void setAdresseEleve(Adresse adresseEleve) {
        this.adresseEleve = adresseEleve;
    }

    public boolean estEleve() {
        return "Eleve".equals(role);
    }

    public boolean estDirecteur() {
        return "Directeur".equals(role);
    }

    public boolean peutModifierProfil() {
        return estEleve() || estDirecteur() || "Formateur".equals(role);
    }
}
