package mg.itu.cryptomonnaie.utils;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(final Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    public static Object getBean(final String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(final String beanName, final Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }

    public static CacheManager cacheManager() {
        return getBean(CacheManager.class);
    }
}
