package mg.itu.cryptomonnaie.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeTransaction implements ValuedEnum {
    ACHAT("Achat"),
    VENTE("Vente");

    private final String value;
}
