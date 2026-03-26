package universite_Paris8.iut.qdev.tp2026.gr12.mock;

import universite_Paris8.iut.qdev.tp2026.gr12.entitites.JoueurDTO;
import universite_Paris8.iut.qdev.tp2026.gr12.interfaces.IJoueurDAO;

import java.util.List;

/**
 * Mock dédié uniquement au test de la fonctionnalité "listerJoueurs".
 */
public class MockListerJoueurs implements IJoueurDAO {

    private List<JoueurDTO> listePredefinie;

    //on passe la fausse liste au mock quand on le crée
    public MockListerJoueurs(List<JoueurDTO> listePredefinie) {
        this.listePredefinie = listePredefinie;
    }

    @Override
    public List<JoueurDTO> listerJoueurs() {
        return listePredefinie; // retourne la fausse liste
    }

    // methodes non utilisées par ce mock
    @Override
    public boolean pseudoDejaUtilise(String pseudo) {
        return false;
    }

    @Override
    public JoueurDTO insererJoueur(JoueurDTO joueur) {
        return null;
    }
}