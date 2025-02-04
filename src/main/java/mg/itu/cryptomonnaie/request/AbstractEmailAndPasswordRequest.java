package mg.itu.cryptomonnaie.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractEmailAndPasswordRequest {
    private String email;

    @JsonProperty("mot_de_passe")
    private String motDePasse;

    public AbstractEmailAndPasswordRequest unsetMotDePasse() {
        motDePasse = null;
        return this;
    }
}
