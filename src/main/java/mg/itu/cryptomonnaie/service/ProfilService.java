package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.repository.ProfilRepository;

import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProfilService {
    private final ProfilRepository profilRepository;

    @Nullable
    @Transactional
    public Profil getByEmail(final String email) {
        return profilRepository.findByEmail(email);
    }

    @Transactional
    public Profil save(final Profil utilisateur) {
        return profilRepository.save(utilisateur);
    }

    @Transactional
    public Profil updateOrCreate(final String email, final String token) {
        Profil utilisateur = getByEmail(email);
        if (utilisateur == null) {
            utilisateur = new Profil();
            utilisateur.setEmail(email);
        }

        utilisateur.setToken(token);
        return save(utilisateur);
    }

    public List<Profil> listeProfils() {
        return profilRepository.findAll();
    }

    public Profil getById(Long id) {
        return profilRepository.findById(id).orElse(null);
    }
}
