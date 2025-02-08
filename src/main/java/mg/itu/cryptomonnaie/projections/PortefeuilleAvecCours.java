package mg.itu.cryptomonnaie.projections;

import java.time.LocalDateTime;

public interface PortefeuilleAvecCours {
    Integer getIdUtilisateur();
    Integer getIdCryptomonnaie();
    Integer getDesignationCryptomonnaie();
    Float   getQuantite();
    Double  getCoursActuel();
    LocalDateTime getDateHeureCours();
}
