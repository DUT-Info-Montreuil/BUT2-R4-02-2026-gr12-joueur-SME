package universite_Paris8.iut.qdev.tp2026.gr12.exceptions;

/** Pseudo déjà utilisé par un autre joueur. */
public class PseudoDejaUtiliseException extends Exception {
    public PseudoDejaUtiliseException(String message) {
        super(message);
    }
}
