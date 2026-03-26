package universite_Paris8.iut.qdev.tp2026.gr12.interfaces;

import universite_Paris8.iut.qdev.tp2026.gr12.entitites.JoueurDTO;

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


}
