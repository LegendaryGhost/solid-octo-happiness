package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriqueTransactionRepository extends JpaRepository<HistoriqueTransaction, Integer> {

    List<HistoriqueTransaction> findAllByUtilisateurIdOrderByDateHeureDesc(Integer idUtilisateur);

    List<HistoriqueTransaction> findAllByOrderByDateHeureDesc();
}
