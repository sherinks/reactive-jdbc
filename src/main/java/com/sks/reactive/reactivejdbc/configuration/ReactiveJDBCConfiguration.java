package com.sks.reactive.reactivejdbc.configuration;


import com.sks.reactive.reactivejdbc.dao.UserDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
public class ReactiveJDBCConfiguration {


    @Bean
    public DataSource dataSource() {

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        DataSource dataSource = builder.setType(H2).ignoreFailedDrops(true).addScript("my-schema.sql").addScript("my-data.sql").build();
        return dataSource;

    }


    @Bean
    public JdbcTemplate jdbcTemplate() {

        return new JdbcTemplate(dataSource());
    }

    @Bean
    public UserDAO userDAO() {

        return new UserDAO(jdbcTemplate());
    }

}
