package nextstep.di.factory;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);
    private static final Class<Configuration> CONFIGURATION = Configuration.class;
    private Set<Class<?>> configureClazz;
    private Set<String> basePackages;
    private BeanFactory beanFactory;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.basePackages = new HashSet<>();
        doScan();
    }

    public void register(Class<?> type) {
        beanFactory.addBeanType(type);
    }

    private void doScan() {
        Reflections reflections = new Reflections("nextstep.di.factory.example");
        this.configureClazz = reflections.getTypesAnnotatedWith(CONFIGURATION);
        scanBasePackages();

    }

    private void scanBasePackages() {
        for (Class<?> type : configureClazz) {
            addBasePackage(type);
            scanMethods(type);
        }
    }

    private void addBasePackage(Class<?> type) {
        if (type.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan annotation = type.getAnnotation(ComponentScan.class);
            this.basePackages.addAll(Arrays.asList(annotation.basePackages()));
        }
    }

    private void scanMethods(Class<?> type) {
        for (Method method : type.getDeclaredMethods()) {
            registerBean(type, method);
        }
    }

    private void registerBean(Class<?> type, Method method) {
        if (method.isAnnotationPresent(Bean.class)) {
            beanFactory.registerBean(method.getReturnType(), createBean(type, method));
        }
    }

    private Object createBean(Class<?> type, Method method) {
        try {
            return method.invoke(type.getDeclaredConstructor().newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new BeanCreateException();
        }
    }
}
