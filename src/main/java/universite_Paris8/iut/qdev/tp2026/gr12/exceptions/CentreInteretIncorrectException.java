package universite_Paris8.iut.qdev.tp2026.gr12.exceptions;

/** Centre d'intérêt vide ou se terminant par une virgule. */
public class CentreInteretIncorrectException extends Exception {
    public CentreInteretIncorrectException(String message) {
        super(message);
    }
}
