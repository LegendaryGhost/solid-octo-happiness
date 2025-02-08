package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

public interface OperationRepository extends JpaRepository<Operation, Integer> {

    List<Operation> findAllByDateHeureEquals(@Nullable LocalDateTime dateHeure);

    List<Operation> findAllByUtilisateurId(Integer idUtilisateur);
}
