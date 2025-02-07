package mg.itu.cryptomonnaie.dto;

import java.util.List;

public record SituationPortefeuilleDTO(
    Integer idUtilisateur,
    Double  fondsActuel,
    List<PortefeuilleAvecCoursDTO> portefeuilleAvecCoursList,
    List<HistoriqueTransactionDTO> historiquesTransaction
) { }
