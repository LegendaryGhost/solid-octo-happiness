package mg.itu.cryptomonnaie.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class Utils {
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

    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    public static HttpSession getCurrentSession() {
        return getCurrentRequest().getSession();
    }
}
