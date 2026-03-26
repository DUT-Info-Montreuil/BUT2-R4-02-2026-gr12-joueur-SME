package universite_Paris8.iut.qdev.tp2026.gr12.interfaces;

import universite_Paris8.iut.qdev.tp2026.gr12.entitites.JoueurDTO;
import universite_Paris8.iut.qdev.tp2026.gr12.entitites.ScoreDTO;

import java.util.List;

/**
 * Interface DAO pour la gestion des joueurs en base de données.
 */
public interface IJoueurDAO {

    /**
     * Vérifie si un pseudo est déjà utilisé.
     *
     * @param pseudo le pseudo à vérifier
     * @return true si le pseudo est déjà pris, false sinon
     */
    boolean pseudoDejaUtilise(String pseudo);

    /**
     * Insère un nouveau joueur en base de données.
     *
     * @param joueur le joueur à insérer
     * @return le joueur avec son idJoueur généré
     */
    JoueurDTO insererJoueur(JoueurDTO joueur);

    /**
     * Récupère la liste de tous les joueurs.
     *
     * @return la liste des joueurs, vide si aucun joueur n'existe
     */
    List<JoueurDTO> listerJoueurs();

    /**
     * Supprime un joueur à partir de son pseudo (en tenant compte de la casse).
     *
     * @param pseudo le pseudo exact du joueur à supprimer
     */
    void supprimerJoueur(String pseudo);

    /**
     * Récupère les informations d'un joueur à partir de son pseudo.
     *
     * @param pseudo le pseudo du joueur recherché
     * @return le JoueurDTO correspondant, ou null si non trouvé
     */
    JoueurDTO recupererJoueurParPseudo(String pseudo);

    /**
     * Enregistre le résultat d'une partie pour un joueur donné.
     *
     * @param pseudo le pseudo du joueur ayant joué
     * @param score le score obtenu à la partie
     * @param tempsEnSecondes la durée de la partie en secondes
     */
    void ajouterScoreJoueur(String pseudo, int score, int tempsEnSecondes);

    /**
     * Récupère l'intégralité des scores enregistrés pour toutes les parties.
     * Note: Doit inclure les scores des joueurs qui ont été supprimés logiquement.
     *
     * @return une liste non triée de tous les ScoreDTO
     */
    List<ScoreDTO> recupererTousLesScores();

    /**
     * Récupère tout l'historique des scores d'un joueur spécifique,
     * trié du plus récent au plus ancien.
     *
     * @param pseudo le pseudo du joueur
     * @return la liste de ses parties (ScoreDTO), ou une liste vide s'il n'a jamais joué
     */
    List<ScoreDTO> recupererScoresJoueur(String pseudo);
}
