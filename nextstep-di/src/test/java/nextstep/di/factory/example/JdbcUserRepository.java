package nextstep.di.factory.example;

import nextstep.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcUserRepository implements UserRepository {
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }
}
