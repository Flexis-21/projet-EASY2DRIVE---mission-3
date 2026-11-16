package fr.lecastel.easy2drive.modele;

public class AutoEcole {
    private final int id;
    private final String raisonSociale;
    private Adresse adresse;

    public AutoEcole(int id, String raisonSociale, Adresse adresse) {
        this.id = id;
        this.raisonSociale = raisonSociale;
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }
}
