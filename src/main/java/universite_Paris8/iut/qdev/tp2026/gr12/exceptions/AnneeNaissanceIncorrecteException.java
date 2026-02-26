package universite_Paris8.iut.qdev.tp2026.gr12.exceptions;

/** L'année de naissance fournie est invalide. */
public class AnneeNaissanceIncorrecteException extends Exception {
    public AnneeNaissanceIncorrecteException(String message) {
        super(message);
    }
}
