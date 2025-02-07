package mg.itu.cryptomonnaie.utils;

import mg.itu.cryptomonnaie.security.AuthenticationManager;

public final class Facade {
    private Facade() { }

    public static AuthenticationManager authenticationManager() {
        return SpringContextUtil.getBean(AuthenticationManager.class);
    }
}
