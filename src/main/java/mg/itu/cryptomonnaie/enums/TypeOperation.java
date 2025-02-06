package mg.itu.cryptomonnaie.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TypeOperation {
    DEPOT("Dépôt"),
    RETRAIT("Retrait");

    private final String value;

    public static TypeOperation fromValue(final String value) {
        return Arrays.stream(TypeOperation.values())
            .filter(typeOperation -> typeOperation.value.equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Aucun type d'opération ne correspond à la valeur : " + value));
    }
}
