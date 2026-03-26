package universite_Paris8.iut.qdev.tp2026.gr12;

import universite_Paris8.iut.qdev.tp2026.gr12.interfaces.IJoueurDAO;
import universite_Paris8.iut.qdev.tp2026.gr12.entitites.JoueurDTO;
import universite_Paris8.iut.qdev.tp2026.gr12.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import universite_Paris8.iut.qdev.tp2026.gr12.services.JoueurService;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour {@link JoueurService#ajouterJoueur(JoueurDTO)}.
 */
class JoueurServiceTest {

    private IJoueurDAO joueurDAO;
    private JoueurService joueurService;

    @BeforeEach
    void setUp() {
        joueurDAO = mock(IJoueurDAO.class);
        joueurService = new JoueurService(joueurDAO);
    }

    // -------------------------------------------------------------------------
    // Cas nominal
    // -------------------------------------------------------------------------

    @Test
    void ajouterJoueur_nominal_retourneJoueur()
            throws PseudoIncorrectException, PseudoDejaUtiliseException,
            AnneeNaissanceIncorrecteException, CentreInteretIncorrectException, JoueurNullException, LanguePrefIncorrecteException {

        JoueurDTO joueur = new JoueurDTO(0, "alice", "Alice", 2000, 1, "musique, sport");
        JoueurDTO joueurCree = new JoueurDTO(1, "alice", "Alice", 2000, 1, "musique, sport");

        when(joueurDAO.pseudoDejaUtilise("alice")).thenReturn(false);
        when(joueurDAO.insererJoueur(joueur)).thenReturn(joueurCree);

        JoueurDTO result = joueurService.ajouterJoueur(joueur);

        assertEquals(1, result.getIdJoueur());
        verify(joueurDAO).insererJoueur(joueur);
    }

    @Test
    void ajouterJoueur_sansPrenomNiAnneeNiCI_nominal()
            throws PseudoIncorrectException, PseudoDejaUtiliseException,
            AnneeNaissanceIncorrecteException, CentreInteretIncorrectException, JoueurNullException, LanguePrefIncorrecteException {

        JoueurDTO joueur = new JoueurDTO(0, "bob", null, null, 3, null);
        JoueurDTO joueurCree = new JoueurDTO(2, "bob", null, null, 3, null);

        when(joueurDAO.pseudoDejaUtilise("bob")).thenReturn(false);
        when(joueurDAO.insererJoueur(joueur)).thenReturn(joueurCree);

        JoueurDTO result = joueurService.ajouterJoueur(joueur);
        assertEquals(2, result.getIdJoueur());
    }

    // -------------------------------------------------------------------------
    // Pseudo incorrect
    // -------------------------------------------------------------------------

    @Test
    void ajouterJoueur_pseudoVide_levePseudoIncorrectException() {
        JoueurDTO joueur = new JoueurDTO(0, "", "Alice", 2000, 1, null);
        assertThrows(PseudoIncorrectException.class, () -> joueurService.ajouterJoueur(joueur));
    }

    @Test
    void ajouterJoueur_pseudoNull_levePseudoIncorrectException() {
        JoueurDTO joueur = new JoueurDTO(0, null, "Alice", 2000, 1, null);
        assertThrows(PseudoIncorrectException.class, () -> joueurService.ajouterJoueur(joueur));
    }

    @Test
    void ajouterJoueur_pseudoCommenceParChiffre_levePseudoIncorrectException() {
        JoueurDTO joueur = new JoueurDTO(0, "1alice", "Alice", 2000, 1, null);
        assertThrows(PseudoIncorrectException.class, () -> joueurService.ajouterJoueur(joueur));
    }

    // -------------------------------------------------------------------------
    // Pseudo déjà utilisé
    // -------------------------------------------------------------------------

    @Test
    void ajouterJoueur_pseudoDejaUtilise_levePseudoDejaUtiliseException() {
        JoueurDTO joueur = new JoueurDTO(0, "alice", "Alice", 2000, 1, null);
        when(joueurDAO.pseudoDejaUtilise("alice")).thenReturn(true);
        assertThrows(PseudoDejaUtiliseException.class, () -> joueurService.ajouterJoueur(joueur));
    }

    // -------------------------------------------------------------------------
    // Année de naissance
    // -------------------------------------------------------------------------

    @Test
    void ajouterJoueur_anneeNaissanceTropAncienne_leveAnneeIncorrecteException() {
        JoueurDTO joueur = new JoueurDTO(0, "alice", "Alice", 1800, 1, null);
        when(joueurDAO.pseudoDejaUtilise("alice")).thenReturn(false);
        assertThrows(AnneeNaissanceIncorrecteException.class,
                     () -> joueurService.ajouterJoueur(joueur));
    }

    @Test
    void ajouterJoueur_anneeDansLeFutur_leveAnneeIncorrecteException() {
        int anneeFuture = Year.now().getValue() + 1;
        JoueurDTO joueur = new JoueurDTO(0, "alice", "Alice", anneeFuture, 1, null);
        when(joueurDAO.pseudoDejaUtilise("alice")).thenReturn(false);
        assertThrows(AnneeNaissanceIncorrecteException.class,
                     () -> joueurService.ajouterJoueur(joueur));
    }

    // -------------------------------------------------------------------------
    // Centre d'intérêt
    // -------------------------------------------------------------------------

    @Test
    void ajouterJoueur_centreInteretFinissantParVirgule_leveCIIncorrectException() {
        JoueurDTO joueur = new JoueurDTO(0, "alice", "Alice", 2000, 1, "sport,");
        when(joueurDAO.pseudoDejaUtilise("alice")).thenReturn(false);
        assertThrows(CentreInteretIncorrectException.class,
                     () -> joueurService.ajouterJoueur(joueur));
    }

    // -------------------------------------------------------------------------
    // Langue préférée
    // -------------------------------------------------------------------------

    @Test
    void ajouterJoueur_langueHorsLimite_leveIllegalArgumentException() {
        JoueurDTO joueur = new JoueurDTO(0, "alice", "Alice", 2000, 6, null);
        when(joueurDAO.pseudoDejaUtilise("alice")).thenReturn(false);
        assertThrows(LanguePrefIncorrecteException.class, () -> joueurService.ajouterJoueur(joueur));
    }

    @Test
    void ajouterJoueur_langueZero_leveIllegalArgumentException() {
        JoueurDTO joueur = new JoueurDTO(0, "alice", "Alice", 2000, 0, null);
        when(joueurDAO.pseudoDejaUtilise("alice")).thenReturn(false);
        assertThrows(LanguePrefIncorrecteException.class, () -> joueurService.ajouterJoueur(joueur));
    }
}
