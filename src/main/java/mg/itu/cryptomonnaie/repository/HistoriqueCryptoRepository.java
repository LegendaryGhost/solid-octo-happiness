package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueCryptoRepository extends JpaRepository<HistoriqueTransaction, Long> {

    @Query("select hc from HistoriqueTransaction hc where hc.profil.id = :idProfil order by hc.dateHeure desc")
    List<HistoriqueTransaction> findAllByProfil(@Param("idProfil") Long idProfil);

    List<HistoriqueTransaction> findAllByOrderByDateActionDesc();

}
