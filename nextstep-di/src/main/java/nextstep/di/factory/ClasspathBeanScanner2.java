package nextstep.di.factory;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClasspathBeanScanner2 extends AbstractBeanScanner {
    public ClasspathBeanScanner2(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    Set<Class<? extends Annotation>> getTargetAnnotation() {
        return new HashSet<>(Arrays.asList(Controller.class, Service.class, Repository.class));
    }
}
