package mg.itu.cryptomonnaie.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mg.itu.cryptomonnaie.enums.TypeAnalyseCommission;

import java.time.LocalDateTime;

@Data
public class AnalyseCommissionRequest {
    @NotNull
    private TypeAnalyseCommission typeAnalyse;

    @NotNull
    private Integer idCryptomonnaie;

    private LocalDateTime dateHeureMin;

    private LocalDateTime dateHeureMax;
}
