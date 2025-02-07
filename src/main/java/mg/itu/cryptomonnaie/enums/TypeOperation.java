package mg.itu.cryptomonnaie.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeOperation implements ValuedEnum {
    DEPOT("Dépôt"),
    RETRAIT("Retrait");

    private final String value;
}
