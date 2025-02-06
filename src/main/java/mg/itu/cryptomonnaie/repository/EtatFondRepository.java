package mg.itu.cryptomonnaie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mg.itu.cryptomonnaie.entity.EtatFond;

@Repository
public interface EtatFondRepository extends JpaRepository<EtatFond, Long> {

}
