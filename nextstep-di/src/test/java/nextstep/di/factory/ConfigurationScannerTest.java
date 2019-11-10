package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationScannerTest {

    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory(new HashSet<>(Arrays.asList(BasicDataSource.class)));
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(BasicDataSource.class));
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(IntegrationConfig.class);
        beanFactory.initialize();

        ClasspathBeanScanner2 cbds = new ClasspathBeanScanner2(beanFactory);
        cbds.doScan("di.examples");

        assertNotNull(beanFactory.getBean(BasicDataSource.class));

        JdbcUserRepository userRepository = beanFactory.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);
        assertNotNull(userRepository.getDataSource());

        MyJdbcTemplate jdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }

}