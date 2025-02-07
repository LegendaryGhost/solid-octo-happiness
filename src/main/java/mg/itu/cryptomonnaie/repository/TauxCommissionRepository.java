package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.TauxCommission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TauxCommissionRepository extends JpaRepository<TauxCommission, Integer> {

    Optional<TauxCommission> findFirstByOrderById();
}
