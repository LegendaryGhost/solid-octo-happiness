package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

public interface HistoriqueFondsRepository extends JpaRepository<HistoriqueFonds, Integer> {

    @Query("select hf from HistoriqueFonds hf where hf.utilisateur.id = :idProfil")
    List<HistoriqueFonds> findTransactionsProfil(@Param("idProfil") Long idProfil);

    List<HistoriqueFonds> findAllByDateHeureEquals(@Nullable LocalDateTime dateHeure);

    @Query("""
        SELECT shf.historiqueFonds
        FROM StatutHistoriqueFonds shf
        WHERE shf.historiqueFonds.id = :id
        ORDER BY shf.dateHeure DESC
    """)
    Optional<HistoriqueFonds> findByIdAndLatestStatut(Integer id);
}
