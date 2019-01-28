package com.sks.reactive.reactivejdbc;


import com.sks.reactive.reactivejdbc.configuration.ReactiveJDBCConfiguration;
import com.sks.reactive.reactivejdbc.dao.UserDAO;
import com.sks.reactive.reactivejdbc.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ReactiveJDBCConfiguration.class)

public class UserDAOTest {


    @Autowired
    UserDAO userDAO;


    @Test
    public void testGetUser(){


        userDAO.getUsers().subscribe(System.out::println,System.out::println);


    }
}
