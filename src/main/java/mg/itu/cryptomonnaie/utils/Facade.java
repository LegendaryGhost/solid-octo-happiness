package mg.itu.cryptomonnaie.utils;

import mg.itu.cryptomonnaie.security.AuthenticationManager;
import org.springframework.data.jpa.repository.JpaRepository;

public final class Facade {
    private Facade() { }

    public static AuthenticationManager authenticationManager() {
        return SpringContextUtil.getBean(AuthenticationManager.class);
    }

    // S'attend que les repository se terminent avec le suffixe "Repository".
    @SuppressWarnings("unchecked")
    public static <T, ID> JpaRepository<T, ID> getRepositoryFor(final Class<T> entityClass) {
        return (JpaRepository<T, ID>) SpringContextUtil.getBean(
            Utils.uncapitalize(entityClass.getSimpleName()) + "Repository");
    }
}
