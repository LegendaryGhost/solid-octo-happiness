package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.Portefeuille;
import mg.itu.cryptomonnaie.projections.PortefeuilleAvecCours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortefeuilleRepository extends JpaRepository<Portefeuille, Integer> {

    Portefeuille findByUtilisateurIdAndCryptomonnaieId(Integer idUtilisateur, Integer idCryptomonnaie);

    @Query("""
        SELECT u.id,
               c.id,
               c.designation,
               p.quantite,
               cc.cours,
               cc.dateHeure
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
    List<PortefeuilleAvecCours> findAvecCoursActuelByUtilisateur(Integer idUtilisateur);
}
