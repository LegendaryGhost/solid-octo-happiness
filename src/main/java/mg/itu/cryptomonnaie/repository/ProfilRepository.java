package mg.itu.cryptomonnaie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mg.itu.cryptomonnaie.entity.Profil;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Long> {

}
