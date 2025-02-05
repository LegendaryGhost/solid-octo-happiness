package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HistoriqueFondsRepository extends JpaRepository<HistoriqueFonds, Integer> {

    @Query("select hf from HistoriqueFonds hf where hf.utilisateur.id = :idProfil")
    List<HistoriqueFonds> findTransactionsProfil(@Param("idProfil") Long idProfil);
}
