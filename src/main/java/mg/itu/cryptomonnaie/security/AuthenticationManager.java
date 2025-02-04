package mg.itu.cryptomonnaie.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.service.ProfilService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import static mg.itu.cryptomonnaie.utils.Utils.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationManager {
    public static final String AUTHENTICATED_USER_KEY = "security.auth.user";
    private final ProfilService profilService;

    public void authenticate(final String email) {
        log.debug("Authentification de l'utilisateur avec l'email : {}", email);
        getCurrentSession().setAttribute(AUTHENTICATED_USER_KEY, profilService.getByEmail(email));
    }

    @Nullable
    public Profil getCurrentUser() {
        return (Profil) getCurrentSession().getAttribute(AUTHENTICATED_USER_KEY);
    }

    public Profil safelyGetCurrentUser() {
        Profil currentUser = getCurrentUser();
        if (currentUser == null) throw new IllegalStateException("Aucun utilisateur connecté");

        return currentUser;
    }

    public boolean isUserConnected() {
        return getCurrentUser() != null;
    }
}
