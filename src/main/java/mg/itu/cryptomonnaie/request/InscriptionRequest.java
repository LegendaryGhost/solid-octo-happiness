package mg.itu.cryptomonnaie.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

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
