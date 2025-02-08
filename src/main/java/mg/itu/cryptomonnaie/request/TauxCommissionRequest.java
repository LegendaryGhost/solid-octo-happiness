package mg.itu.cryptomonnaie.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TauxCommissionRequest {
    @NotNull
    @Positive
    private Double valeurAchat;

    @NotNull
    @Positive
    private Double valeurVente;
}
