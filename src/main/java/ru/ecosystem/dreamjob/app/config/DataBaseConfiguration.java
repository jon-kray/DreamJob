package ru.ecosystem.dreamjob.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;

@Configuration
@Profile(Profiles.PROD)
public class DataBaseConfiguration {

    @Bean
    @Primary
    public DataSource dataSource(Environment environment) {
        var driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(requireNonNull(environment.getProperty(getKeyByPrefix("driver"))));
        driverManagerDataSource.setUrl(requireNonNull(environment.getProperty(getKeyByPrefix("url"))));
        driverManagerDataSource.setUsername(requireNonNull(environment.getProperty(getKeyByPrefix("username"))));
        driverManagerDataSource.setPassword(requireNonNull(environment.getProperty(getKeyByPrefix("password"))));
        return driverManagerDataSource;
    }

    private String getKeyByPrefix(String key) {
        return String.format("config.database.%s", key);
    }
}
