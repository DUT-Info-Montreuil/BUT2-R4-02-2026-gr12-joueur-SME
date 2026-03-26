package universite_Paris8.iut.qdev.tp2026.gr12.mock;

import universite_Paris8.iut.qdev.tp2026.gr12.entitites.JoueurDTO;
import universite_Paris8.iut.qdev.tp2026.gr12.interfaces.IJoueurDAO;

import java.util.List;

/**
 * Mock dédié uniquement au test de la fonctionnalité "insererJoueur".
 */
public class MockInsererJoueur implements IJoueurDAO {

    private boolean methodeAppelee = false;

    @Override
    public JoueurDTO insererJoueur(JoueurDTO joueur) {
        this.methodeAppelee = true;
        //on simule que la BD lui a donné l'ID 1
        joueur.setIdJoueur(1);
        return joueur;
    }

    public boolean isMethodeAppelee() {
        return methodeAppelee;
    }

    // methodes non utilisées par ce mock
    @Override
    public boolean pseudoDejaUtilise(String pseudo) {
        return false;
    }

    @Override
    public List<JoueurDTO> listerJoueurs() {
        return null;
    }
}