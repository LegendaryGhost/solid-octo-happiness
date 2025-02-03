package mg.itu.cryptomonnaie.component;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.service.ProfilService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public final class AuthenticationManager {
    private final ProfilService profilService;

    public static void login() {

    }
}
