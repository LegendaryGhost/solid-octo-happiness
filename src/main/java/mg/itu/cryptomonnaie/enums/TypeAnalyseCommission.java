package mg.itu.cryptomonnaie.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeAnalyseCommission implements ValuedEnum {
    SOMME("Somme"),
    MOYENNE("Moyenne");

    private final String value;
}
