package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import mg.itu.cryptomonnaie.enums.StatutOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface OperationRepository extends JpaRepository<Operation, Integer> {

    @Query("""
        SELECT o
        FROM Operation o
            JOIN SuiviOperation so ON o.id = so.operation.id
        WHERE so.dateHeure = (SELECT MAX(so2.dateHeure) FROM SuiviOperation so2 WHERE so2.operation.id = o.id)
            AND so.statut = :statut
            AND (COALESCE(:dateHeure, o.dateHeure) = o.dateHeure)
    """)
    List<Operation> findAllBySuiviOperationRecentAndStatut(@Nullable LocalDateTime dateHeure, StatutOperation statut);

    List<Operation> findAllByUtilisateurId(Integer idUtilisateur);
}
