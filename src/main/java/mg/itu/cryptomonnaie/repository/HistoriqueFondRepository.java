package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueFondRepository extends JpaRepository<HistoriqueFonds, Long> {
    @Query("select hf from HistoriqueFonds hf where hf.profil.id = :idProfil")
    List<HistoriqueFonds> findTransactionsProfil(@Param("idProfil") Long idProfil);
}