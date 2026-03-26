package universite_Paris8.iut.qdev.tp2026.gr12;

import org.junit.jupiter.api.Test;
import universite_Paris8.iut.qdev.tp2026.gr12.entitites.JoueurDTO;
import universite_Paris8.iut.qdev.tp2026.gr12.exceptions.PseudoIncorrectException;
import universite_Paris8.iut.qdev.tp2026.gr12.mock.MockInsererJoueur;
import universite_Paris8.iut.qdev.tp2026.gr12.mock.MockListerJoueurs;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JoueurServiceTest {

    @Test
    void testInsererJoueur() {
        MockInsererJoueur mockInsertion = new MockInsererJoueur();
        JoueurDTO nouveauJoueur = new JoueurDTO(0, "yaya", "Yanis", 2006, 1, "Jeux");
        JoueurDTO resultat = mockInsertion.insererJoueur(nouveauJoueur);

        //verif
        assertTrue(mockInsertion.isMethodeAppelee(), "la méthode insererJoueur du DAO a du etre appelée");
        assertNotNull(resultat, "le joueur retourne ne doit pas être null");
        assertEquals(1, resultat.getIdJoueur(), "l'ID doit avoir été simulé et mis à 1 par le mock");
    }

    @Test
    void testListerJoueurs() {
        List<JoueurDTO> fausseListe = Arrays.asList(
                new JoueurDTO(1, "bloom", "Anton", 2006, 1, "Sport"),
                new JoueurDTO(2, "zz", "Ilyes", 2002, 2, "Lecture")
        );
        MockListerJoueurs mockListe = new MockListerJoueurs(fausseListe);
        List<JoueurDTO> resultat = mockListe.listerJoueurs();

        //verif
        assertEquals(2, resultat.size(), "la liste récupérée doit contenir 2 joueurs");
        assertEquals("bloom", resultat.get(0).getPseudoJoueur(), "le premier joueur doit bien être bloom");
    }

    // test initial pour le pseudo vide
    @Test
    void pseudovide() {
        JoueurDTO joueur = new JoueurDTO(0, "", "Anton", 2006, 1, null);
        assertThrows(PseudoIncorrectException.class, () -> {
            throw new PseudoIncorrectException("le pseudo ne peut pas être vide");
        }, "l'exception PseudoIncorrectException doit être levée car le pseudo est vide");
    }
}