package mg.itu.cryptomonnaie.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import mg.itu.cryptomonnaie.enums.TypeOperation;

@Data
public class OperationRequest {
    @NotNull
    @NotBlank
    private String numCarteBancaire;

    @NotNull
    @Positive
    private Double montant;

    @NotNull
    private TypeOperation typeOperation;
}
