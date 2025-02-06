package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursCryptoRepository extends JpaRepository<CoursCrypto, Integer> {
    CoursCrypto findFirstByCryptomonnaieIdOrderByDateHeureDesc(Integer idCryptomonnaie);

    List<CoursCrypto> findByCryptomonnaieId(Integer idCryptomonnaie);

    List<CoursCrypto> findAllByCryptomonnaieIdInAndDateHeureBetweenOrderByDateHeureDesc(
        List<Integer> idsCryptomonnaie, LocalDateTime dateHeureMin, LocalDateTime dateHeureMax);
}
