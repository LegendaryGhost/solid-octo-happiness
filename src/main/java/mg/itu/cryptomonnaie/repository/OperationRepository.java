package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface OperationRepository extends JpaRepository<Operation, Integer> {

    @Query("""
        SELECT o
        FROM Operation o
            JOIN SuiviOperation so ON o.id = so.operation.id
        WHERE so.dateHeure = (SELECT MAX(so2.dateHeure) FROM SuiviOperation so2 WHERE so2.operation.id = o.id)
            AND so.statut = mg.itu.cryptomonnaie.enums.StatutOperation.VALIDEE
            AND (COALESCE(:dateHeure, o.dateHeure) = o.dateHeure)
    """)
    List<Operation> findAllBySuiviOperationRecentAndStatutValidee(@Nullable LocalDateTime dateHeure);

    List<Operation> findAllByUtilisateurId(Integer idUtilisateur);
}
