package nextstep.di.factory;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationBeanScanner {
    private static final Class<Configuration> CONFIGURATION = Configuration.class;
    private Set<Class<?>> types;
    private Set<String> basePackages;
    private BeanFactory beanFactory;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        doScan();
    }

    private void doScan() {
        Reflections reflections = new Reflections("nextstep.di.factory.example");
        this.types = reflections.getTypesAnnotatedWith(CONFIGURATION);
        try {
            scanBasePackages();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void scanBasePackages() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Class<?> type : types) {
            if (type.isAnnotationPresent(ComponentScan.class)) {
                ComponentScan annotation = type.getAnnotation(ComponentScan.class);
                this.basePackages = new HashSet<>(Arrays.asList(annotation.basePackages()));

                for (Method method : type.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        Object invoke = method.invoke(type.getDeclaredConstructor().newInstance());
                        beanFactory.registerBean(method.getReturnType(), invoke);
                    }
                }
            }
        }
    }

    public void register(Class<?> type) {
        beanFactory.addBeanType(type);
    }
}
