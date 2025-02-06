package mg.itu.cryptomonnaie.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Utils {
    public static final String BINDING_RESULT_KEY_PREFIX = "org.springframework.validation.BindingResult.";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static HttpHeaders createJsonHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }

    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    public static HttpSession getCurrentSession() {
        return getCurrentRequest().getSession();
    }

    public static String snakeToCamelCase(final String string) {
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

    public static String generateToken(final int length) {
        SecureRandom secureRandom = new SecureRandom();

        return IntStream.range(0, length)
            .mapToObj(i ->
                String.valueOf(
                    CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length()))
                ))
            .collect(Collectors.joining());
    }

    public static Cache safelyGetCache(final String name, final CacheManager cacheManager) {
        Cache cache = cacheManager.getCache(name);
        if (cache == null) throw new RuntimeException("Aucune cache trouvée avec le nom : " + name);

        return cache;
    }
}
