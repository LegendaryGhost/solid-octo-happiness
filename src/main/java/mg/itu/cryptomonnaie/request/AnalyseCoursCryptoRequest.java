package mg.itu.cryptomonnaie.request;

import lombok.Data;
import mg.itu.cryptomonnaie.enums.TypeAnalyseCoursCrypto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnalyseCoursCryptoRequest {
    private TypeAnalyseCoursCrypto typeAnalyseCoursCrypto;

    private List<Integer> idsCryptomonnaie;

    private LocalDateTime dateHeureMin;

    private LocalDateTime dateHeureMax;
}
