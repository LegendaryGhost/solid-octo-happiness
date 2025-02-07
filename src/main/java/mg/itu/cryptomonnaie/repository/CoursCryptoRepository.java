package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface CoursCryptoRepository extends JpaRepository<CoursCrypto, Integer> {

    List<CoursCrypto> findByCryptomonnaieId(Integer idCryptomonnaie);

    CoursCrypto findFirstByCryptomonnaieIdOrderByDateHeureDesc(Integer idCryptomonnaie);

    @Query("""
        SELECT cc.cours
        FROM CoursCrypto cc
        WHERE cc.cryptomonnaie.id IN :idsCryptomonnaie
            AND (:dateHeureMin IS NULL OR cc.dateHeure >= :dateHeureMin)
            AND (:dateHeureMax IS NULL OR cc.dateHeure <= :dateHeureMax)
        ORDER BY cc.dateHeure DESC
    """)
    List<Double> findAllCoursActuelInIdsCryptomonnaieForPeriode(
        List<Integer> idsCryptomonnaie,
        @Nullable LocalDateTime dateHeureMin,
        @Nullable LocalDateTime dateHeureMax);
}
