package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.entity.Profil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilRepository extends JpaRepository<Profil, Long> {

    Profil findByEmail(String email);
}
