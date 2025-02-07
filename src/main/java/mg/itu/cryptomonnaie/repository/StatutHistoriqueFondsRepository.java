package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.StatutHistoriqueFonds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatutHistoriqueFondsRepository extends JpaRepository<StatutHistoriqueFonds, Integer> {

    Optional<StatutHistoriqueFonds> findByHistoriqueFondsIdOrderByDateHeureDesc(Integer idHistoriqueFonds);
}
