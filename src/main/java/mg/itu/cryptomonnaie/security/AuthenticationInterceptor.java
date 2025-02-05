package mg.itu.cryptomonnaie.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean preHandle(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler
    ) throws Exception {
        // Uncomment the code below when pushing to production
        /* if (authenticationManager.isUserConnected()) return true;

        response.sendRedirect("/connexion");
        return false; */

        if (!authenticationManager.isUserConnected()) authenticationManager.authenticate("example@domain.com");
        return true;
    }
}
