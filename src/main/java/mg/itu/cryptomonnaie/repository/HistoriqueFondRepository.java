package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueFondRepository extends JpaRepository<HistoriqueFond, Long> {
    @Query("select hf from HistoriqueFond hf where hf.profil.id = :idProfil")
    List<HistoriqueFond> findTransactionsProfil(@Param("idProfil") Long idProfil);
}