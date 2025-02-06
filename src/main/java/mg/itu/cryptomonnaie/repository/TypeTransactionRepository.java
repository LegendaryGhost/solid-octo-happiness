package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.TypeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeTransactionRepository extends JpaRepository<TypeTransaction, Integer> { }
