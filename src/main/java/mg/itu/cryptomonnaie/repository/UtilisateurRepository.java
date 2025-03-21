package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

    Utilisateur findByEmail(String email);
}
