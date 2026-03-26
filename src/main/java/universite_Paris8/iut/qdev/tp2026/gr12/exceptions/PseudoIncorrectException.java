    package universite_Paris8.iut.qdev.tp2026.gr12.exceptions;

    /** Pseudo vide, null ou commençant par un chiffre. */
    public class PseudoIncorrectException extends Exception {
        public PseudoIncorrectException(String message) {
            super(message);
        }
    }
