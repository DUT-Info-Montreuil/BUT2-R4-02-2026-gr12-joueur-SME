package universite_Paris8.iut.qdev.tp2026.gr12.entitites;

/**
 * Objet métier représentant un joueur.
 */
public class JoueurDTO {

    private int idJoueur;

    /** Ne peut pas être vide, doit être unique, ne doit pas commencer par un nombre. */
    private String pseudoJoueur;

    /** Peut être vide. */
    private String prenomJoueur;

    /** Peut être vide. */
    private Integer anneeNaissance;

    /** Entre 1 et 5. */
    private int languePref;

    /** Peut être vide. Ne peut pas finir par ','. */
    private String centreInteret;

    public JoueurDTO() {}

    public JoueurDTO(int idJoueur, String pseudoJoueur, String prenomJoueur,
                     Integer anneeNaissance, int languePref, String centreInteret) {
        this.idJoueur = idJoueur;
        this.pseudoJoueur = pseudoJoueur;
        this.prenomJoueur = prenomJoueur;
        this.anneeNaissance = anneeNaissance;
        this.languePref = languePref;
        this.centreInteret = centreInteret;
    }

    // Getters & Setters

    public int getIdJoueur() { return idJoueur; }
    public void setIdJoueur(int idJoueur) { this.idJoueur = idJoueur; }

    public String getPseudoJoueur() { return pseudoJoueur; }
    public void setPseudoJoueur(String pseudoJoueur) { this.pseudoJoueur = pseudoJoueur; }

    public String getPrenomJoueur() { return prenomJoueur; }
    public void setPrenomJoueur(String prenomJoueur) { this.prenomJoueur = prenomJoueur; }

    public Integer getAnneeNaissance() { return anneeNaissance; }
    public void setAnneeNaissance(Integer anneeNaissance) { this.anneeNaissance = anneeNaissance; }

    public int getLanguePref() { return languePref; }
    public void setLanguePref(int languePref) { this.languePref = languePref; }

    public String getCentreInteret() { return centreInteret; }
    public void setCentreInteret(String centreInteret) { this.centreInteret = centreInteret; }

    @Override
    public String toString() {
        return "JoueurDTO{" +
                "idJoueur=" + idJoueur +
                ", pseudoJoueur='" + pseudoJoueur + '\'' +
                ", prenomJoueur='" + prenomJoueur + '\'' +
                ", anneeNaissance=" + anneeNaissance +
                ", languePref=" + languePref +
                ", centreInteret='" + centreInteret + '\'' +
                '}';
    }
}
