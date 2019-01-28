package com.sks.reactive.reactivejdbc;

import com.sks.reactive.reactivejdbc.configuration.ReactiveJDBCConfiguration;
import com.sks.reactive.reactivejdbc.dao.UserDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ReactiveJDBCConfiguration.class)
@SpringBootTest
public class ReactiveJdbcApplicationTests {


    @Autowired
    UserDAO dao;

    @Test
	public void contextLoads() {
	}

	@Test
	public void testGetUsers(){

        dao.getUsers();

    }

}

