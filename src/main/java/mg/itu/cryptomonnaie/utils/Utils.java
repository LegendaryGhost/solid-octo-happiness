package mg.itu.cryptomonnaie.utils;

import jakarta.servlet.http.HttpSession;
import mg.itu.cryptomonnaie.entity.Profil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

public final class Utils {
    public static final String USER_KEY = "connected_user";
    public static final String BINDING_RESULT_KEY_PREFIX = "org.springframework.validation.BindingResult.";

    public static HttpHeaders createJsonHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }

    public static String snakeToCamelCase(String string) {
        final StringBuilder stringBuilder = new StringBuilder();
        boolean nextUpperCase = false;

        for (char c : string.toCharArray()) {
            if (c == '_') nextUpperCase = true;
            else {
                stringBuilder.append(nextUpperCase ? Character.toUpperCase(c) : c);
                nextUpperCase = false;
            }
        }

        return stringBuilder.toString();
    }

    @Deprecated
    @Nullable
    public static Profil getUser(HttpSession httpSession) {
        return (Profil) httpSession.getAttribute(USER_KEY);
    }
}
