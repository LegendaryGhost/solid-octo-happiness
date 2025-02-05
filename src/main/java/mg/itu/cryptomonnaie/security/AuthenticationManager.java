package mg.itu.cryptomonnaie.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.service.UtilisateurService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import static mg.itu.cryptomonnaie.utils.Utils.*;

@Slf4j
@RequiredArgsConstructor
@Getter
@Component
public class AuthenticationManager {
    public static final String AUTHENTICATED_USER_KEY = "security.auth.user";
    private final UtilisateurService utilisateurService;

    public void authenticate(final String email) {
        log.debug("Authentification d'utilisateur avec l'email : {}", email);
        getCurrentSession().setAttribute(AUTHENTICATED_USER_KEY, utilisateurService.getByEmail(email));
    }

    @Nullable
    public Utilisateur getCurrentUser() {
        return (Utilisateur) getCurrentSession().getAttribute(AUTHENTICATED_USER_KEY);
    }

    public Utilisateur safelyGetCurrentUser() {
        Utilisateur currentUser = getCurrentUser();
        if (currentUser == null) throw new IllegalStateException("Aucun utilisateur connecté à récupérer");

        return currentUser;
    }

    public boolean isUserConnected() {
        return getCurrentUser() != null;
    }
}
