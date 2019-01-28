package com.sks.reactive.reactivejdbc.dao;

import com.sks.reactive.reactivejdbc.domain.User;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import org.reactivestreams.Subscriber;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {


    private String GET_ALL_USERS="select * from user";

    JdbcTemplate template;

    public UserDAO(JdbcTemplate template){

        this.template = template;
    }

    public Observable<User> getUsers() {


        List<User> allUsers = template.query(GET_ALL_USERS,(resultSet)->{

            List<User> users = new ArrayList<>();
            while (resultSet.next()){
                User user =
                        new User(resultSet.getInt("userID"), resultSet.getString("name"), resultSet.getInt("age"));

                System.out.println(user);
                users.add(user);
            }
            return users;
        });

       System.out.println(allUsers);

        return template.query(GET_ALL_USERS,

                ( resultSet)-> {
                   return Observable.create(new ObservableOnSubscribe<User>() {
                        @Override
                        public void subscribe(ObservableEmitter<User> emitter) throws Exception {

                            while (resultSet.next()&&emitter.isDisposed()) {
                                User user =
                                        new User(resultSet.getInt("userID"), resultSet.getString("name"), resultSet.getInt("age"));
                                emitter.onNext(user);
                            }
                            emitter.onComplete();

                        }
                    });

                }
                );

    }
}
