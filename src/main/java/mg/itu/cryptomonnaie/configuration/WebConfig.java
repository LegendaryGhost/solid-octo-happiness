package mg.itu.cryptomonnaie.configuration;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.security.AdminAuthenticationInterceptor;
import mg.itu.cryptomonnaie.security.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationInterceptor authenticationInterceptor;
    private final AdminAuthenticationInterceptor adminAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Maybe should we list the paths one by one ?
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns("/portefeuille", "/operation/**", "/transaction/creation",
                "/historique-fond/**", "/cours/**");

        registry.addInterceptor(adminAuthenticationInterceptor)
            .addPathPatterns("/admin/**");
    }
}
