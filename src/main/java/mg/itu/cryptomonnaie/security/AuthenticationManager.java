package mg.itu.cryptomonnaie.security;

import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static mg.itu.cryptomonnaie.utils.Utils.*;

@Slf4j
@Component
public class AuthenticationManager {
    public static final String AUTHENTICATED_USER_KEY = "security.auth.user";

    public void authenticate(final Utilisateur utilisateur) {
        Assert.notNull(utilisateur, "L'utilisateur à authentifier ne peut pas être \"null\"");

        log.debug("Authentification d'utilisateur avec l'email : {}", utilisateur.getEmail());
        getCurrentSession().setAttribute(AUTHENTICATED_USER_KEY, utilisateur);
    }

    public void logout() {
        getCurrentSession().removeAttribute(AUTHENTICATED_USER_KEY);
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
