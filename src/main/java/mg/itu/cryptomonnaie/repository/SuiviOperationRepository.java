package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.SuiviOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuiviOperationRepository extends JpaRepository<SuiviOperation, Integer> {

    Optional<SuiviOperation> findByOperationIdOrderByDateHeureDesc(Integer idOperation);
}
