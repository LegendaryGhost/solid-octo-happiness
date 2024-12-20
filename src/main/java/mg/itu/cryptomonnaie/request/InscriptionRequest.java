package mg.itu.cryptomonnaie.request;

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
    private LocalDate dateNaissance;
    private String motDePasse;
}
