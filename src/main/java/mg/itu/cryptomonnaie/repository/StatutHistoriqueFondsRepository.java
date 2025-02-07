package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.SuiviOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatutHistoriqueFondsRepository extends JpaRepository<SuiviOperation, Integer> {

    Optional<SuiviOperation> findByHistoriqueFondsIdOrderByDateHeureDesc(Integer idHistoriqueFonds);
}
