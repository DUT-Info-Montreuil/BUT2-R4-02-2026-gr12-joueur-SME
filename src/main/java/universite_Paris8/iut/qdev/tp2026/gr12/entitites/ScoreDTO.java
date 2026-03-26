package universite_Paris8.iut.qdev.tp2026.gr12.entitites;

public class ScoreDTO {
    private String pseudoJoueur;
    private int score;
    private int tempsEnSecondes;

    public ScoreDTO(String pseudoJoueur, int score, int tempsEnSecondes) {
        this.pseudoJoueur = pseudoJoueur;
        this.score = score;
        this.tempsEnSecondes = tempsEnSecondes;
    }

    public String getPseudoJoueur() { return pseudoJoueur; }
    public int getScore() { return score; }
    public int getTempsEnSecondes() { return tempsEnSecondes; }
}