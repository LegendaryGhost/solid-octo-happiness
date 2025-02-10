package mg.itu.cryptomonnaie.exception;

import lombok.Getter;

@Getter
public class NoSuchCollection extends RuntimeException {
    private final Class<?> clazz;

    public NoSuchCollection(final Class<?> clazz) {
        super(String.format("Aucune collection trouvée avec la classe \"%s\"", clazz.getName()));
        this.clazz = clazz;
    }
}
