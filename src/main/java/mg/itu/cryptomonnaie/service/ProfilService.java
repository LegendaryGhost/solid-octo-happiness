package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.repository.ProfilRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfilService {
    private final ProfilRepository profilRepository;

    public Profil getByEmail(String email) {
        return profilRepository.findByEmail(email);
    }
}
