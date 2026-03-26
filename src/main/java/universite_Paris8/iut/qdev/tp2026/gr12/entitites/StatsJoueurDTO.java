package universite_Paris8.iut.qdev.tp2026.gr12.entitites;

import java.util.List;

public class StatsJoueurDTO {
    private int nbPartiesJouees;
    private int nbBonnesReponsesTotal;
    private double moyenneGenerale;
    private int dureeMoyenneSecondes;
    private List<ScoreDTO> dernieresParties;

    public StatsJoueurDTO(int nbPartiesJouees, int nbBonnesReponsesTotal,
                          double moyenneGenerale, int dureeMoyenneSecondes,
                          List<ScoreDTO> dernieresParties) {
        this.nbPartiesJouees = nbPartiesJouees;
        this.nbBonnesReponsesTotal = nbBonnesReponsesTotal;
        this.moyenneGenerale = moyenneGenerale;
        this.dureeMoyenneSecondes = dureeMoyenneSecondes;
        this.dernieresParties = dernieresParties;
    }

    // Getters pour que l'interface puisse lire les stats
    public int getNbPartiesJouees() { return nbPartiesJouees; }
    public int getNbBonnesReponsesTotal() { return nbBonnesReponsesTotal; }
    public double getMoyenneGenerale() { return moyenneGenerale; }
    public int getDureeMoyenneSecondes() { return dureeMoyenneSecondes; }
    public List<ScoreDTO> getDernieresParties() { return dernieresParties; }
}