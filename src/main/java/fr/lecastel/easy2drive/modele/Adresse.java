package fr.lecastel.easy2drive.modele;

public class Adresse {
    private final String ville;
    private final String codePostal;
    private final String rue;

    public Adresse(String ville, String codePostal, String rue) {
        this.ville = ville;
        this.codePostal = codePostal;
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public String getRue() {
        return rue;
    }
}
