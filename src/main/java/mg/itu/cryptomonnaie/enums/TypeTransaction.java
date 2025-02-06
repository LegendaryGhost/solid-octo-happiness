package mg.itu.cryptomonnaie.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeTransaction {
    ACHAT("Achat"),
    VENTE("Vente");

    private final String value;
}
