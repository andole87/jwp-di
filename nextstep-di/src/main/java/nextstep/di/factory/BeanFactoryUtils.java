package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Inject;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;

public class BeanFactoryUtils {
    private static final int JUST_ONE = 1;

    /**
     * 인자로 전달하는 클래스의 생성자 중 @Inject 애노테이션이 설정되어 있거나 하나만 있는 생성자를 반환
     *
     * @param clazz
     * @return
     * @Inject 애노테이션이 설정되어 있는 생성자는 클래스당 하나로 가정한다.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Optional<Constructor<?>> getInjectedConstructor(Class<?> clazz) {
        List<Constructor<?>> cons = Arrays.asList(clazz.getConstructors());
        if (hasOnlyDefaultConstructor(cons)) {
            return Optional.of(cons.get(0));
        }

        return cons.stream()
                .filter(withAnnotation(Inject.class))
                .findAny();
    }

    private static boolean hasOnlyDefaultConstructor(List<Constructor<?>> cons) {
        return cons.size() == JUST_ONE;
    }

    /**
     * 인자로 전달되는 클래스의 구현 클래스. 만약 인자로 전달되는 Class가 인터페이스가 아니면 전달되는 인자가 구현 클래스,
     * 인터페이스인 경우 BeanFactory가 관리하는 모든 클래스 중에 인터페이스를 구현하는 클래스를 찾아 반환
     *
     * @param injectedClazz
     * @param preInstantiateBeans
     * @return
     */
    public static Class<?> findConcreteClass(Class<?> injectedClazz, Set<Class<?>> preInstantiateBeans) {
        if (!injectedClazz.isInterface()) {
            return injectedClazz;
        }

        for (Class<?> clazz : preInstantiateBeans) {
            Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces());
            if (interfaces.contains(injectedClazz)) {
                return clazz;
            }
        }

        throw new BeanCreateException(injectedClazz + "인터페이스를 구현하는 Bean이 존재하지 않는다.");
    }

    public static Optional<Class<?>> findConcreteClass2(Class<?> injectedClazz, Set<Class<?>> preInstantiateBeans) {
        if (!injectedClazz.isInterface()) {
            return Optional.of(injectedClazz);
        }

        for (Class<?> clazz : preInstantiateBeans) {
            Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces());
            if (interfaces.contains(injectedClazz)) {
                return Optional.of(clazz);
            }
        }

        return Optional.empty();
    }
}
