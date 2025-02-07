package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

public interface HistoriqueFondsRepository extends JpaRepository<HistoriqueFonds, Integer> {

    @Query("select hf from HistoriqueFonds hf where hf.utilisateur.id = :idProfil")
    List<HistoriqueFonds> findTransactionsProfil(@Param("idProfil") Long idProfil);

    List<HistoriqueFonds> findAllByDateHeureEquals(@Nullable LocalDateTime dateHeure);
}
