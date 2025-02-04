package mg.itu.cryptomonnaie.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConnexionRequest {
    private String email;

    @JsonProperty("mot_de_passe")
    private String motDePasse;
}
