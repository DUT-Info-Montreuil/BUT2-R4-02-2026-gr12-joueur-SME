package universite_Paris8.iut.qdev.tp2026.gr12.services;

import universite_Paris8.iut.qdev.tp2026.gr12.entitites.ScoreDTO;
import universite_Paris8.iut.qdev.tp2026.gr12.entitites.StatsJoueurDTO;
import universite_Paris8.iut.qdev.tp2026.gr12.interfaces.IJoueurDAO;
import universite_Paris8.iut.qdev.tp2026.gr12.entitites.JoueurDTO;
import universite_Paris8.iut.qdev.tp2026.gr12.exceptions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des joueurs.
 */
public class JoueurService {

    private static final int ANNEE_MIN = 1900;
    private static final int LANGUE_MIN = 1;
    private static final int LANGUE_MAX = 5;

    private final IJoueurDAO joueurDAO;

    public JoueurService(IJoueurDAO joueurDAO) {
        this.joueurDAO = joueurDAO;
    }

    // -------------------------------------------------------------------------
    // Use case : ajouterJoueur
    // -------------------------------------------------------------------------

    /**
     * Crée un nouveau compte joueur après validation de toutes les règles métier.
     *
     * <p>Règles appliquées (dans l'ordre du diagramme d'étapes fonctionnelles) :
     * <ol>
     *   <li>Le pseudo ne doit pas être vide et ne doit pas commencer par un chiffre.</li>
     *   <li>Le pseudo ne doit pas déjà exister en base.</li>
     *   <li>Le prénom peut être vide (aucune validation bloquante).</li>
     *   <li>L'année de naissance, si renseignée, doit être comprise entre
     *       {@value #ANNEE_MIN} et l'année courante incluse.</li>
     *   <li>Le centre d'intérêt, si renseigné, ne doit pas se terminer par ','.</li>
     *   <li>La langue préférée doit être comprise entre {@value #LANGUE_MIN}
     *       et {@value #LANGUE_MAX}.</li>
     * </ol>
     *
     * @param joueur le DTO contenant les informations du joueur à créer
     * @return le DTO du joueur créé, avec son identifiant généré
     * @throws PseudoIncorrectException          si le pseudo est vide ou commence par un chiffre
     * @throws PseudoDejaUtiliseException        si le pseudo est déjà pris
     * @throws AnneeNaissanceIncorrecteException si l'année de naissance est invalide
     * @throws CentreInteretIncorrectException   si le centre d'intérêt se termine par ','
     * @throws IllegalArgumentException          si la langue préférée est hors de [1-5]
     */
    public JoueurDTO ajouterJoueur(JoueurDTO joueur)
            throws PseudoIncorrectException,
                   PseudoDejaUtiliseException,
                   AnneeNaissanceIncorrecteException,
                   CentreInteretIncorrectException,
                   LanguePrefIncorrecteException,
                   JoueurNullException {

        if(joueur == null) {
            throw new JoueurNullException("Joueur ne peut pas être null");
        }

        // --- Étape 1 : Validation du pseudo (format) ---
        validerPseudo(joueur.getPseudoJoueur());

        // --- Étape 2 : Vérification unicité du pseudo ---
        if (joueurDAO.pseudoDejaUtilise(joueur.getPseudoJoueur())) {
            throw new PseudoDejaUtiliseException(
                    "Le pseudo '" + joueur.getPseudoJoueur() + "' est déjà utilisé.");
        }

        // --- Étape 3 : Validation de l'année de naissance ---
        if (joueur.getAnneeNaissance() != null) {
            validerAnneeNaissance(joueur.getAnneeNaissance());
        }

        // --- Étape 4 : Validation du centre d'intérêt ---
        if (joueur.getCentreInteret() != null && !joueur.getCentreInteret().isBlank()) {
            validerCentreInteret(joueur.getCentreInteret());
        }

        // --- Étape 5 : Validation de la langue préférée ---
        validerLanguePref(joueur.getLanguePref());

        // --- Persistance ---
        return joueurDAO.insererJoueur(joueur);
    }


    /**
     * Retourne la liste de tous les joueurs enregistrés.
     *
     * @return la liste des joueurs, vide si aucun joueur n'existe
     */
    public List<JoueurDTO> listerJoueurs() {
        List<JoueurDTO> listeNonTriee = joueurDAO.listerJoueurs();

        return listeNonTriee.stream().
                sorted(Comparator.comparing(JoueurDTO::getPseudoJoueur, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Use case : supprimerJoueur
    // -------------------------------------------------------------------------

    /**
     * Supprime un joueur existant en vérifiant son pseudo.
     * La casse doit être strictement respectée.
     * * @param pseudo le pseudo du joueur à supprimer
     * @throws PseudoIncorrectException si le pseudo fourni est vide ou invalide
     * @throws JoueurInexistantException si aucun joueur ne correspond à ce pseudo
     */

    public void supprimerJoueur(String pseudo)
            throws PseudoIncorrectException, JoueurInexistantException {

        // 1. Validation de base du format du pseudo
        if (pseudo == null || pseudo.isBlank()) {
            throw new PseudoIncorrectException("Le pseudo à supprimer ne peut pas être vide.");
        }

        // 2. Vérification de l'existence du joueur (en tenant compte de la casse)
        // On réutilise pseudoDejaUtilise() qui sert déjà à vérifier l'existence
        if (!joueurDAO.pseudoDejaUtilise(pseudo)) {
            throw new JoueurInexistantException("Impossible de supprimer : le joueur '" + pseudo + "' n'existe pas.");
        }

        // 3. Suppression via le DAO
        joueurDAO.supprimerJoueur(pseudo);
    }


    // -------------------------------------------------------------------------
    // Use case : transmettreInfoJoueur
    // -------------------------------------------------------------------------

    /**
     * Récupère le profil complet d'un joueur via son pseudo.
     * * @param pseudo le pseudo exact du joueur
     * @return le JoueurDTO contenant toutes les infos
     * @throws PseudoIncorrectException si le pseudo est vide
     * @throws JoueurInexistantException si le joueur n'existe pas en base
     */
    public JoueurDTO transmettreInfoJoueur(String pseudo)
            throws PseudoIncorrectException, JoueurInexistantException {

        // 1. Validation de la saisie
        if (pseudo == null || pseudo.isBlank()) {
            throw new PseudoIncorrectException("Le pseudo recherché ne peut pas être vide.");
        }

        // 2. Récupération via le DAO
        JoueurDTO joueur = joueurDAO.recupererJoueurParPseudo(pseudo);

        // 3. Vérification de l'existence
        if (joueur == null) {
            throw new JoueurInexistantException("Le joueur '" + pseudo + "' est introuvable.");
        }

        return joueur;
    }


    // -------------------------------------------------------------------------
    // Use case : gestionScoreJoueur
    // -------------------------------------------------------------------------

    /**
     * Enregistre le score et le temps d'un joueur à la fin d'une partie.
     *
     * @param pseudo le pseudo exact du joueur
     * @param score le score final calculé
     * @param tempsEnSecondes le temps mis pour répondre aux 10 questions
     * @throws PseudoIncorrectException si le pseudo est vide
     * @throws JoueurInexistantException si le joueur n'existe pas en base
     * @throws scoreNegatifException si le score est négatifs
     * @throws tempsNegatifException si le temps est négatif
     */
    public void gestionScoreJoueur(String pseudo, int score, int tempsEnSecondes)
            throws PseudoIncorrectException, JoueurInexistantException, scoreNegatifException, tempsNegatifException {

        // 1. Validation de la saisie du pseudo
        if (pseudo == null || pseudo.isBlank()) {
            throw new PseudoIncorrectException("Le pseudo ne peut pas être vide pour enregistrer un score.");
        }

        // 2. Vérification de l'existence du joueur
        if (!joueurDAO.pseudoDejaUtilise(pseudo)) {
            throw new JoueurInexistantException("Impossible d'enregistrer le score : le joueur '" + pseudo + "' est introuvable.");
        }

        // 3. Validation des données de la partie
        if (score < 0) {
            throw new scoreNegatifException("Le score ne peut pas être négatif.");
        }
        if (tempsEnSecondes < 0) {
            throw new tempsNegatifException("Le temps de la partie ne peut pas être négatif.");
        }

        // 4. Enregistrement via le DAO
        joueurDAO.ajouterScoreJoueur(pseudo, score, tempsEnSecondes);
    }


    // -------------------------------------------------------------------------
    // Use case : fournirClassement
    // -------------------------------------------------------------------------

    /**
     * Calcule et renvoie le Top 3 des meilleures parties.
     * Règles : tri par score décroissant, puis par temps croissant.
     * Limité à 3 résultats.
     *
     * @return La liste des 3 meilleurs ScoreDTO (ou moins s'il y a peu de parties)
     */
    public List<ScoreDTO> fournirClassement() {

        List<ScoreDTO> tousLesScores = joueurDAO.recupererTousLesScores();

        // Sécurité si la base est vide
        if (tousLesScores == null || tousLesScores.isEmpty()) {
            return List.of(); // Retourne une liste vide immuable
        }

        // Règle métier : Tri et limitation (Top 3)
        return tousLesScores.stream()
                // 1. Tri principal : Score décroissant (du plus grand au plus petit)
                .sorted(Comparator.comparingInt(ScoreDTO::getScore).reversed()
                        // 2. Tri secondaire : Temps croissant (du plus rapide au plus lent)
                        .thenComparingInt(ScoreDTO::getTempsEnSecondes))
                // 3. Limite : On ne garde que les 3 premiers
                .limit(3)
                // On regroupe le tout dans une liste
                .collect(Collectors.toList());
    }


    // -------------------------------------------------------------------------
    // Use case : fournirStatsJoueur
    // -------------------------------------------------------------------------

    /**
     * Calcule et renvoie les statistiques globales d'un joueur.
     *
     * @param pseudo le pseudo du joueur
     * @return StatsJoueurDTO contenant les calculs, ou un objet vide si 0 parties.
     * @throws PseudoIncorrectException si le pseudo est vide
     * @throws JoueurInexistantException si le joueur n'existe pas
     */

    public StatsJoueurDTO fournirStatsJoueur(String pseudo)
            throws PseudoIncorrectException, JoueurInexistantException {

        // 1. Validations de base (on réutilise ce qu'on a fait avant)
        if (pseudo == null || pseudo.isBlank()) {
            throw new PseudoIncorrectException("Le pseudo ne peut pas être vide.");
        }
        if (!joueurDAO.pseudoDejaUtilise(pseudo)) {
            throw new JoueurInexistantException("Impossible de charger les stats : le joueur '" + pseudo + "' n'existe pas.");
        }

        // 2. Récupération de l'historique du joueur
        List<ScoreDTO> historique = joueurDAO.recupererScoresJoueur(pseudo);

        // 3. Cas : "Pas encore joué"
        if (historique == null || historique.isEmpty()) {
            // On renvoie un objet avec 0 partie. L'interface applicative saura qu'elle doit afficher "Pas encore joué".
            return new StatsJoueurDTO(0, 0, 0.0, 0, List.of());
        }

        // 4. Calculs sur l'ENSEMBLE des parties
        int nbParties = historique.size();
        int scoreTotal = 0;
        int tempsTotal = 0;

        for (ScoreDTO partie : historique) {
            scoreTotal += partie.getScore();
            tempsTotal += partie.getTempsEnSecondes();
        }

        // Arrondi de la moyenne à 2 chiffres après la virgule
        double moyenneBrute = (double) scoreTotal / nbParties;
        double moyenneArrondie = new BigDecimal(moyenneBrute)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        // Arrondi de la durée moyenne à la seconde (division entière par défaut)
        int dureeMoyenne = Math.round((float) tempsTotal / nbParties);

        // 5. Récupération des 5 DERNIÈRES parties uniquement
        List<ScoreDTO> dernieresParties = historique.stream()
                .limit(5)
                .collect(Collectors.toList());

        // 6. On retourne notre bel objet de stats !
        return new StatsJoueurDTO(
                nbParties,
                scoreTotal,
                moyenneArrondie,
                dureeMoyenne,
                dernieresParties
        );
    }
    // -------------------------------------------------------------------------
    // Méthodes de validation privées
    // -------------------------------------------------------------------------

    /**
     * Valide le format du pseudo.
     * Règles : non null, non vide, ne commence pas par un chiffre.
     */
    private void validerPseudo(String pseudo) throws PseudoIncorrectException {
        if (pseudo == null || pseudo.isBlank()) {
            throw new PseudoIncorrectException("Le pseudo ne peut pas être vide.");
        }
        if (Character.isDigit(pseudo.charAt(0))) {
            throw new PseudoIncorrectException(
                    "Le pseudo '" + pseudo + "' ne doit pas commencer par un chiffre.");
        }
    }

    /**
     * Valide l'année de naissance.
     * Règles : >= {@value #ANNEE_MIN} et <= année courante.
     */
    private void validerAnneeNaissance(int annee) throws AnneeNaissanceIncorrecteException {
        int anneeCourante = Year.now().getValue();
        if (annee < ANNEE_MIN || annee > anneeCourante) {
            throw new AnneeNaissanceIncorrecteException(
                    "L'année de naissance " + annee + " est incorrecte. "
                    + "Elle doit être comprise entre " + ANNEE_MIN + " et " + anneeCourante + ".");
        }
    }

    /**
     * Valide le centre d'intérêt.
     * Règle : ne doit pas se terminer par ','.
     */
    private void validerCentreInteret(String centreInteret) throws CentreInteretIncorrectException {
        if (centreInteret.trim().endsWith(",")) {
            throw new CentreInteretIncorrectException(
                    "Le centre d'intérêt ne peut pas se terminer par ','.");
        }
    }

    /**
     * Valide la langue préférée.
     * Règle : doit être comprise entre {@value #LANGUE_MIN} et {@value #LANGUE_MAX}.
     */
    private void validerLanguePref(int langue) throws LanguePrefIncorrecteException {
        if (langue < LANGUE_MIN || langue > LANGUE_MAX) {
            throw new LanguePrefIncorrecteException(
                    "La langue préférée doit être comprise entre "
                    + LANGUE_MIN + " et " + LANGUE_MAX + ". Valeur reçue : " + langue);
        }
    }
}
