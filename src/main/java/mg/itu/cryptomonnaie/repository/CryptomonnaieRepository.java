package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptomonnaieRepository extends JpaRepository<Cryptomonnaie, Integer> { }
