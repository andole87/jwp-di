package nextstep.di.factory;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClasspathBeanScanner {
    private static Set<Class<? extends Annotation>> TARGET_ANNOTATION = new HashSet<>() {{
        add(Controller.class);
        add(Service.class);
        add(Repository.class);
    }};
    private Reflections reflections;
    private BeanFactory beanFactory;

    public ClasspathBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return new BeanFactory(scanBeans());
    }

    public void doScan(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
        beanFactory.addAllBeanType(scanBeans());
    }

    private Set<Class<?>> scanBeans() {
        return TARGET_ANNOTATION.stream()
                .map(type -> reflections.getTypesAnnotatedWith(type))
                .flatMap(sets -> sets.stream())
                .collect(Collectors.toSet());
    }
}
