package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeActionRepository extends JpaRepository<TypeOperation, Long> {

}
