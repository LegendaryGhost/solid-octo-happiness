package mg.itu.cryptomonnaie.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InscriptionRequest {
    private String email;

    private String nom;

    private String prenom;

    @JsonProperty("date_naissance")
    private LocalDate dateNaissance;

    @JsonProperty("mot_de_passe")
    private String motDePasse;
}
