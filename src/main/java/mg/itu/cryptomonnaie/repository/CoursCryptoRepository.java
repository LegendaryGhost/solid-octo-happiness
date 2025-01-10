package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursCryptoRepository extends JpaRepository<CoursCrypto, Long> {
    @Query("SELECT c FROM CoursCrypto c WHERE c.cryptomonnaie.id = :idCrypto ORDER BY c.dateCours DESC")
    CoursCrypto findCoursActuel(@Param("idCrypto") Long idCrypto);

    @Query("SELECT c FROM CoursCrypto c WHERE c.cryptomonnaie.id = :idCrypto")
    List<CoursCrypto> findCoursParCryptomonnaie(@Param("idCrypto") Long idCrypto);

    @Query("SELECT c " +
        "FROM CoursCrypto c " +
        "WHERE c.cryptomonnaie.id IN :idCryptos " +
        "AND c.dateCours BETWEEN :dateHeureMin AND :dateHeureMax ORDER BY c.dateCours, c.id")
    List<CoursCrypto> findParCryptomonnaieEtDate(
        @Param("idCryptos") List<Long> idCryptos,
        @Param("dateHeureMin") LocalDateTime dateHeureMin,
        @Param("dateHeureMax") LocalDateTime dateHeureMax
    );
}
