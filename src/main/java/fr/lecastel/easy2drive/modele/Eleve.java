public class Eleve
    
    private int id;
    private string dateNaissance;
    private string rue;
    private string codePostal;
    private string ville;
    private string dateInscription;
    private int idAutoEcole;

    /**
     * Construit un élève
     * @param id id de l'utilisateur
     * @param nom nom utilisateur
     * @param prenom prénom utilisateur
     */
    public Eleve(int id, String nom, String prenom, String email, bool estDirecteur, bool estSecretaire, int idAutoEcole) {
        this.id = id;
        this.dateNaissance = dateNaissance;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.dateInscription = dateInscription;
        this.idAutoEcole = idAutoEcole;
    }