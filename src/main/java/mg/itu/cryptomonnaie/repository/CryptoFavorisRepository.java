package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.CryptoFavoris;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoFavorisRepository extends JpaRepository<CryptoFavoris, Integer> {

    boolean existsByUtilisateurIdAndCryptomonnaieId(Integer idUtilisateur, Integer idCryptomonnaie);
}
