package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursCryptoRepository extends JpaRepository<CoursCrypto, Long> {
    @Query("SELECT c FROM CoursCrypto c WHERE c.cryptomonnaie.id = :idCrypto ORDER BY c.dateCours DESC")
    CoursCrypto findCoursActuel(@Param("idCrypto") Long idCrypto);
}
