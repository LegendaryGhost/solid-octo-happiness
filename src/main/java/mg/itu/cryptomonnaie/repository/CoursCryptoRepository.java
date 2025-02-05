package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoursCryptoRepository extends JpaRepository<CoursCrypto, Long> {
    CoursCrypto findFirstByCryptomonnaieIdOrderByDateCoursDesc(Long idCrypto);

    @Query("SELECT c FROM CoursCrypto c WHERE c.cryptomonnaie.id = :idCrypto")
    List<CoursCrypto> findCoursParCryptomonnaie(@Param("idCrypto") Long idCrypto);

    @Query("SELECT c " +
        "FROM CoursCrypto c " +
        "WHERE c.cryptomonnaie.id IN :idCryptos " +
        "AND c.dateHeure BETWEEN :dateHeureMin AND :dateHeureMax ORDER BY c.dateHeure, c.id")
    List<CoursCrypto> findParCryptomonnaieEtDate(
        @Param("idCryptos") List<Long> idCryptos,
        @Param("dateHeureMin") LocalDateTime dateHeureMin,
        @Param("dateHeureMax") LocalDateTime dateHeureMax
    );
}
