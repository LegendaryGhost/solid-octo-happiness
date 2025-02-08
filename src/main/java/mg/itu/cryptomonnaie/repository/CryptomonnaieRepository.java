package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface CryptomonnaieRepository extends JpaRepository<Cryptomonnaie, Integer> {

    @Query("""
        SELECT c
        FROM Cryptomonnaie c
        WHERE (:id IS NULL OR c.id = :id)
        ORDER BY c.id ASC
        LIMIT 1
    """)
    Optional<Cryptomonnaie> findByIdOrFindFirst(@Nullable Integer id);
}
