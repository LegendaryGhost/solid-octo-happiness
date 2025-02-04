package mg.itu.cryptomonnaie.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
@EnableCaching
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("historiqueFondCache");
    }

    @Bean
    public ParameterizedTypeReference<Map<String, Object>> mapTypeReference() {
        return new ParameterizedTypeReference<>() { };
    }
}
