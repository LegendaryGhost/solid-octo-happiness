package mg.itu.cryptomonnaie.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.repository.UtilisateurRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    @Nullable
    @Transactional
    public Utilisateur getByEmail(final String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Transactional
    public Utilisateur save(final Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public Utilisateur updateOrCreate(final String email, final String token) {
        Utilisateur utilisateur = getByEmail(email);
        if (utilisateur == null) {
            utilisateur = new Utilisateur();
            utilisateur.setEmail(email);
        }

        utilisateur.setToken(token);
        return save(utilisateur);
    }
}
