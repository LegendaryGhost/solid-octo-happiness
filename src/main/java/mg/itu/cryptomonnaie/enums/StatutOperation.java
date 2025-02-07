package mg.itu.cryptomonnaie.enums;

public enum StatutOperation {
    EN_ATTENTE,
    VALIDEE,
    REJETEE;

    public static StatutOperation defaultValue() {
        return EN_ATTENTE;
    }
}
