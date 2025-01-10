package mg.itu.cryptomonnaie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.repository.ProfilRepository;

@Service
public class ProfilService {
    @Autowired
    private ProfilRepository profilRepository;

    public Profil getProfilConnecte() {
        Long id = 1L;
        Profil profil = profilRepository.findById(id).orElse(null);
        return profil;
    }

}
