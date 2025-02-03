package mg.itu.cryptomonnaie.configuration;

import mg.itu.cryptomonnaie.component.IsAuthenticatedInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IsAuthenticatedInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/connexion", "/inscription");
    }
}
