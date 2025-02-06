package mg.itu.cryptomonnaie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class PortefeuilleAvecCoursDTO {
    private Integer idUtilisateur;
    private Integer idCryptomonnaie;
    private String  designationCryptomonnaie;
    private Float   quantite;
    private Double  coursActuel;
    private LocalDateTime dateHeureCours;
}
