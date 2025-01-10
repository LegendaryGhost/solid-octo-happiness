package mg.itu.cryptomonnaie;

import jakarta.servlet.http.HttpSession;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.service.ProfilService;
import org.springframework.lang.Nullable;

public final class Utils {
    public static final String USER_KEY = "connected_user";

    public static void login(
        String email,
        ProfilService profilService,
        HttpSession httpSession
    ) {
        Profil profil = profilService.getByEmail(email);
        httpSession.setAttribute(USER_KEY, profil);
    }

    @Nullable
    public static Profil getUser(HttpSession httpSession) {
        return (Profil) httpSession.getAttribute(USER_KEY);
    }

    public static String toCamelCase(String snakeCase) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean nextUpperCase = false;

        for (char c : snakeCase.toCharArray()) {
            if (c == '_') nextUpperCase = true;
            else {
                stringBuilder.append(nextUpperCase ? Character.toUpperCase(c) : c);
                nextUpperCase = false;
            }
        }

        return stringBuilder.toString();
    }
}
