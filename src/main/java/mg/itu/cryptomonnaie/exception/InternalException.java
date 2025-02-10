package mg.itu.cryptomonnaie.exception;

/**
 * Représente une erreur en interne qui normalement ne devrait pas se produire, mais on préfère la prudence.
 */
public class InternalException extends RuntimeException {
    public InternalException() { super(); }
}
