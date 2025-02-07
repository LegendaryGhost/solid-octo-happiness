package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

public interface CoursCryptoRepository extends JpaRepository<CoursCrypto, Long> {
    CoursCrypto findFirstByCryptomonnaieIdOrderByDateCoursDesc(Long idCrypto);

    @Query("SELECT c FROM CoursCrypto c WHERE c.cryptomonnaie.id = :idCrypto")
    List<CoursCrypto> findCoursParCryptomonnaie(@Param("idCrypto") Long idCrypto);

    @Query("""
        SELECT cc.coursActuel
        FROM CoursCrypto cc
        WHERE cc.cryptomonnaie.id IN :idsCryptomonnaie
            AND (:dateHeureMin IS NULL OR cc.dateCours >= :dateHeureMin)
            AND (:dateHeureMax IS NULL OR cc.dateCours <= :dateHeureMax)
        ORDER BY cc.dateCours DESC
    """)
    List<Double> findAllCoursActuelInIdsCryptomonnaieForPeriode(
        List<Integer> idsCryptomonnaie,
        @Nullable LocalDateTime dateHeureMin,
        @Nullable LocalDateTime dateHeureMax);
}
