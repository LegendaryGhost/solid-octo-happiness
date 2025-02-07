package mg.itu.cryptomonnaie.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import mg.itu.cryptomonnaie.enums.TypeTransaction;

@Data
public class HistoriqueTransactionRequest {
    @NotNull
    @Positive
    private Float quantite;

    @NotNull
    private Integer idCryptomonnaie;

    @NotNull
    private TypeTransaction typeTransaction;
}
