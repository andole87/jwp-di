package nextstep.di.factory;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractBeanScanner {
    private Reflections reflections;
    private BeanFactory beanFactory;

    public AbstractBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public AbstractBeanScanner() {
    }

    public void doScan(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
        beanFactory.addAllBeanType(scanBeans());
    }

    private Set<Class<?>> scanBeans() {
        return getTargetAnnotation().stream()
                .map(type -> reflections.getTypesAnnotatedWith(type))
                .flatMap(sets -> sets.stream())
                .collect(Collectors.toSet());
    }

    abstract Set<Class<? extends Annotation>> getTargetAnnotation();

    public void register(Class<?> type) {
        beanFactory.addBeanType(type);
    }
}
