package mg.itu.cryptomonnaie.security;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.service.ProfilService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import static mg.itu.cryptomonnaie.utils.Utils.*;

@RequiredArgsConstructor
@Component
public class AuthenticationManager {
    public static final String AUTHENTICATED_USER_KEY = "security.auth.user";
    private final ProfilService profilService;

    public void authenticate(final String email) {
        getCurrentSession().setAttribute(AUTHENTICATED_USER_KEY, profilService.getByEmail(email));
    }

    @Nullable
    public Profil getCurrentUser() {
        return (Profil) getCurrentSession().getAttribute(AUTHENTICATED_USER_KEY);
    }

    public boolean isUserConnected() {
        return getCurrentUser() != null;
    }
}
