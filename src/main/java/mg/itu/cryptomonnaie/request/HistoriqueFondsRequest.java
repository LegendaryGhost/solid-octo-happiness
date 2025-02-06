package mg.itu.cryptomonnaie.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class HistoriqueFondsRequest {
    @NotNull
    @NotEmpty
    private String numCarteBancaire;

    @NotNull
    @Positive
    private Double montant;

    @NotNull
    private Integer idTypeOperation;
}
