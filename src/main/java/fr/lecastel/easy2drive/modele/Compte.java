package fr.lecastel.easy2drive.modele;

public class Compte {
    private final int id;
    private final String hash;
    private final String sel;

    public Compte(int id, String hash, String sel) {
        this.id = id;
        this.hash = hash;
        this.sel = sel;
    }

    public int getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public String getSel() {
        return sel;
    }
}
