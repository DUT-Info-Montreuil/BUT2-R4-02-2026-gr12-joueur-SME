package universite_Paris8.iut.qdev.tp2026.gr12.services;

import universite_Paris8.iut.qdev.tp2026.gr12.interfaces.IJoueurDAO;
import universite_Paris8.iut.qdev.tp2026.gr12.entitites.JoueurDTO;
import exception.*;

import java.time.Year;

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
                   CentreInteretIncorrectException {

        // --- Étape 1 : Validation du pseudo (format) ---
        validerPseudo(joueur.getPseudoJoueur());

        // --- Étape 2 : Vérification unicité du pseudo ---
        if (joueurDAO.pseudoDejaUtilise(joueur.getPseudoJoueur())) {
            throw new PseudoDejaUtiliseException(
                    "Le pseudo '" + joueur.getPseudoJoueur() + "' est déjà utilisé.");
        }

        // --- Étape 3 : Le prénom peut être vide – pas de validation bloquante ---
        // (Selon le diagramme, si prénom est vide on lève une erreur.
        //  Cependant la spec page 2 indique "Peut être vide".
        //  On se fie à la spec écrite et on accepte un prénom vide.)

        // --- Étape 4 : Validation de l'année de naissance ---
        if (joueur.getAnneeNaissance() != null) {
            validerAnneeNaissance(joueur.getAnneeNaissance());
        }

        // --- Étape 5 : Validation du centre d'intérêt ---
        if (joueur.getCentreInteret() != null && !joueur.getCentreInteret().isBlank()) {
            validerCentreInteret(joueur.getCentreInteret());
        }

        // --- Étape 6 : Validation de la langue préférée ---
        validerLanguePref(joueur.getLanguePref());

        // --- Persistance ---
        return joueurDAO.insererJoueur(joueur);
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
    private void validerLanguePref(int langue) {
        if (langue < LANGUE_MIN || langue > LANGUE_MAX) {
            throw new IllegalArgumentException(
                    "La langue préférée doit être comprise entre "
                    + LANGUE_MIN + " et " + LANGUE_MAX + ". Valeur reçue : " + langue);
        }
    }
}
