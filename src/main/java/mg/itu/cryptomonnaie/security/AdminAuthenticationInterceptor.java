package mg.itu.cryptomonnaie.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class AdminAuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        HttpSession session = request.getSession(false); // Ne crée pas une nouvelle session
        Object connectedAdmin = (session != null) ? session.getAttribute("admin") : null;

        if (connectedAdmin == null) {
            response.sendRedirect("/admin-auth");
            return false;
        }

        return true;
    }
}
