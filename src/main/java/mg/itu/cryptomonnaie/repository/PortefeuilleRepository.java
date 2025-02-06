package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.dto.PortefeuilleAvecCoursDTO;
import mg.itu.cryptomonnaie.entity.Portefeuille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortefeuilleRepository extends JpaRepository<Portefeuille, Integer> {

    @Query("""
        SELECT NEW mg.itu.cryptomonnaie.dto.PortefeuilleAvecCoursDTO(
            u.id,
            c.id,
            c.designation,
            p.quantite,
            cc.coursActuel,
            cc.dateHeure
        )
        FROM Portefeuille p
            JOIN p.cryptomonnaie c
            JOIN CoursCrypto cc ON cc.cryptomonnaie.id = c.id
            JOIN p.utilisateur u
        WHERE cc.dateHeure = (
            SELECT MAX(cc2.dateHeure)
            FROM CoursCrypto cc2
            WHERE cc2.cryptomonnaie.id = c.id
        )
        AND u.id = :idUtilisateur
    """)
    List<PortefeuilleAvecCoursDTO> findAvecCoursActuel(Integer idUtilisateur);
}
