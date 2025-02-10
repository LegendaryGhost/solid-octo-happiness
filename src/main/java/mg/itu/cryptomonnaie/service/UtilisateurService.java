package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.repository.UtilisateurRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    @Transactional
    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

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
    public Utilisateur updateOrCreate(
        final String email,
        final String token,
        final Map<String, String> informations
    ) {
        Utilisateur utilisateur = getByEmail(email);
        if (utilisateur == null) {
            utilisateur = new Utilisateur();
            utilisateur.setId(informations.get("id"));
            utilisateur.setEmail(email);
            utilisateur.setNom(informations.get("nom"));
            utilisateur.setPrenom(informations.get("prenom"));
            utilisateur.setDateNaissance(LocalDate.parse(informations.get("date_naissance")));

            // Infos Nullables
            utilisateur.setPdp(informations.getOrDefault("pdp", null));
            utilisateur.setExpoPushToken(informations.getOrDefault("expo_push_token", null));
        }

        utilisateur.setIdentityFlowToken(token);
        return save(utilisateur);
    }
}
