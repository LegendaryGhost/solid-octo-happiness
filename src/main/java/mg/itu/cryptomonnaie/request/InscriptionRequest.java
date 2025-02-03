package mg.itu.cryptomonnaie.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class InscriptionRequest extends AbstractPasswordInput {
    private String email;

    private String nom;

    private String prenom;

    @JsonProperty("date_naissance")
    private LocalDate dateNaissance;
}
