package mg.itu.cryptomonnaie.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmailAndPasswordRequest {
    private String email;

    @JsonProperty("mot_de_passe")
    private String motDePasse;

    public EmailAndPasswordRequest unsetMotDePasse() {
        motDePasse = null;
        return this;
    }
}
