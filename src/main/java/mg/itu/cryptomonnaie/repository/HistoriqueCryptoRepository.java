package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.dto.ResultatAnalyseCommissionDTO;
import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import mg.itu.cryptomonnaie.enums.TypeAnalyseCommission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

public interface HistoriqueCryptoRepository
        extends JpaRepository<HistoriqueCrypto, Long>, JpaSpecificationExecutor<HistoriqueCrypto> {

    @Query("select hc from HistoriqueCrypto hc where hc.profil.id = :idProfil order by hc.dateAction desc")
    List<HistoriqueCrypto> findAllByProfil(@Param("idProfil") Long idProfil);

    List<HistoriqueCrypto> findAllByOrderByDateActionDesc();

    @Query("""
        SELECT NEW mg.itu.cryptomonnaie.dto.ResultatAnalyseCommissionDTO(
            c.designation,
            CASE WHEN :typeAnalyse = mg.itu.cryptomonnaie.enums.TypeAnalyseCommission.SOMME 
                 THEN CAST(SUM(CASE WHEN hc.typeAction.id = 1 THEN hc.montantCommission ELSE 0 END) AS Double)
                 ELSE CAST(AVG(CASE WHEN hc.typeAction.id = 1 THEN hc.montantCommission ELSE 0 END) AS Double)
            END,
            CASE WHEN :typeAnalyse = mg.itu.cryptomonnaie.enums.TypeAnalyseCommission.SOMME 
                 THEN CAST(SUM(CASE WHEN hc.typeAction.id = 2 THEN hc.montantCommission ELSE 0 END) AS Double)
                 ELSE CAST(AVG(CASE WHEN hc.typeAction.id = 2 THEN hc.montantCommission ELSE 0 END) AS Double)
            END
        )
        FROM HistoriqueCrypto hc
            JOIN hc.cryptomonnaie c
        WHERE (:idCryptomonnaie IS NULL OR c.id = :idCryptomonnaie)
            AND (:dateHeureMin  IS NULL OR hc.dateAction >= :dateHeureMin)
            AND (:dateHeureMax  IS NULL OR hc.dateAction <= :dateHeureMax)
        GROUP BY c.id
    """)
    ResultatAnalyseCommissionDTO analyserCommission(
        TypeAnalyseCommission typeAnalyse,
        @Nullable Integer idCryptomonnaie,
        @Nullable LocalDateTime dateHeureMin,
        @Nullable LocalDateTime dateHeureMax);
}
