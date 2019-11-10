package nextstep.di.factory;

import nextstep.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationBeanScanner extends AbstractBeanScanner {
    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    Set<Class<? extends Annotation>> getTargetAnnotation() {
        return new HashSet<>() {{
            add(Configuration.class);
        }};
    }
}
