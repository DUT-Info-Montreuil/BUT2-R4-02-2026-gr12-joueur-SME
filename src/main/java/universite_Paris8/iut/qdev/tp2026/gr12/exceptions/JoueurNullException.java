package universite_Paris8.iut.qdev.tp2026.gr12.exceptions;

/* Joueur ne peut pas être null */
public class JoueurNullException extends Exception{
    public JoueurNullException(String message) {
        super(message);
    }
}
