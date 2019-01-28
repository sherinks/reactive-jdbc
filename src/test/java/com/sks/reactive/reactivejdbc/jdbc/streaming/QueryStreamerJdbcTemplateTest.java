package com.sks.reactive.reactivejdbc.jdbc.streaming;

import com.sks.reactive.reactivejdbc.configuration.ReactiveJDBCConfiguration;
import com.sks.reactive.reactivejdbc.domain.User;
import io.reactivex.Observable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ReactiveJDBCConfiguration.class)

public class QueryStreamerJdbcTemplateTest {

    @Autowired
    DataSource dataSource;

@Test
public void testQuery(){

    QueryStreamerJdbcTemplate template = new QueryStreamerJdbcTemplate(dataSource);

    Observable<User> userObservable = template.stream("select * from User",
            (resultSet, rowNumber) -> new User(resultSet.getInt("userID"), resultSet.getString("name"), resultSet.getInt("age")));

    userObservable.subscribe(System.out::println);

}


}