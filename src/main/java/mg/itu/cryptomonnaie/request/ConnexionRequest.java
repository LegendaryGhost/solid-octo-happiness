package mg.itu.cryptomonnaie.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConnexionRequest {
    private String email;
    private String motDePasse;
}
