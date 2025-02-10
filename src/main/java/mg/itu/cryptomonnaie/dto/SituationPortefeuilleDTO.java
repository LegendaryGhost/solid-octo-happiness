package mg.itu.cryptomonnaie.dto;

import mg.itu.cryptomonnaie.projections.PortefeuilleAvecCours;

import java.util.List;

public record SituationPortefeuilleDTO(
    String  idUtilisateur,
    Double  fondsActuel,
    List<PortefeuilleAvecCours>    portefeuilleAvecCoursList,
    List<HistoriqueTransactionDTO> historiquesTransaction
) { }
