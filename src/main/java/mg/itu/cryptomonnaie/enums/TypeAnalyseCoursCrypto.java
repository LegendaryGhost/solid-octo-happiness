package mg.itu.cryptomonnaie.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeAnalyseCoursCrypto implements ValuedEnum {
    PREMIER_QUARTILE("Premier quartile"),
    MAX("Max"),
    MIN("Min"),
    MOYENNE("Moyenne"),
    ECART_TYPE("Écart-Type");

    private final String value;
}
