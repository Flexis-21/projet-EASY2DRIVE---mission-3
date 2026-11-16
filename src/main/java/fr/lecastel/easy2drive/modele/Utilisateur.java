public class Utilisateur

    private int id;
    private string nom;
    private string prenom;
    private string email;
    private bool estDirecteur;
    private bool estSecretaire;
    private int idAutoEcole;

    /**
     * Construit un utilisateur.
     * @param id id de l'utilisateur
     * @param nom nom utilisateur
     * @param prenom prénom utilisateur
     */
    public Utilisateur(int id, String nom, String prenom, String email, bool estDirecteur, bool estSecretaire, int idAutoEcole) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.estDirecteur = estDirecteur;
        this.estSecretaire = estSecretaire;
        this.idAutoEcole = idAutoEcole;
    }