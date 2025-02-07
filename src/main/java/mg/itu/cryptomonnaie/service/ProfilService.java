package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.repository.ProfilRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfilService {
    private final ProfilRepository profilRepository;

    public Profil getByEmail(final String email) {
        return profilRepository.findByEmail(email);
    }

    public List<Profil> listeProfils() {
        return profilRepository.findAll();
    }

    public Profil getById(Long id) {
        return profilRepository.findById(id).orElse(null);
    }
}
