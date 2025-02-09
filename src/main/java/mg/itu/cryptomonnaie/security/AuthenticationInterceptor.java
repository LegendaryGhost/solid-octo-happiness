package mg.itu.cryptomonnaie.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.service.UtilisateurService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationManager authenticationManager;

    // We don't actually need this service in production
    private final UtilisateurService utilisateurService;

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        // Uncomment the code below when pushing to production
        /*
         * if (authenticationManager.isUserConnected()) return true;
         * 
         * response.sendRedirect("/connexion");
         * return false;
         */

        // Warning !! : This code is for testing purposes only
        if (!authenticationManager.isUserConnected())
            authenticationManager.authenticate(
                    utilisateurService.updateOrCreate("example@domain.com", "unsecure_token_for_testing_purposes"));
        return true;
    }
}
