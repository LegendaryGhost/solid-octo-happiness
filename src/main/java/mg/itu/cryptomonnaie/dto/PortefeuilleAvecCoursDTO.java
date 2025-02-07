package mg.itu.cryptomonnaie.dto;

import java.time.LocalDateTime;

public record PortefeuilleAvecCoursDTO(
    Integer idUtilisateur,
    Integer idCryptomonnaie,
    String  designationCryptomonnaie,
    Float   quantite,
    Double  coursActuel,
    LocalDateTime dateHeureCours
) { }
