package mg.itu.cryptomonnaie.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mg.itu.cryptomonnaie.enums.TypeAnalyseCoursCrypto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnalyseCoursCryptoRequest {
    @NotNull
    private TypeAnalyseCoursCrypto typeAnalyseCoursCrypto;

    @NotNull
    @NotEmpty
    private List<Integer> idsCryptomonnaie;

    private LocalDateTime dateHeureMin;

    private LocalDateTime dateHeureMax;
}
