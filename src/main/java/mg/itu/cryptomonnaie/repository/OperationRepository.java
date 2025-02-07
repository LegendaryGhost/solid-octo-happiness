package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface OperationRepository extends JpaRepository<Operation, Integer> {

    List<Operation> findAllByDateHeureEquals(@Nullable LocalDateTime dateHeure);

    List<Operation> findAllByUtilisateurId(Integer idUtilisateur);

    @Query("""
        SELECT shf.operation
        FROM StatutHistoriqueFonds shf
        WHERE shf.operation.id = :id
        ORDER BY shf.dateHeure DESC
    """)
    Optional<Operation> findByIdAndLatestStatut(Integer id);
}
