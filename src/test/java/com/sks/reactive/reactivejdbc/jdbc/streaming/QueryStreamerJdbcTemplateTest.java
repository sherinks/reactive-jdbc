package com.sks.reactive.reactivejdbc.jdbc.streaming;

import com.sks.reactive.reactivejdbc.configuration.ReactiveJDBCConfiguration;
import com.sks.reactive.reactivejdbc.domain.User;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.concurrent.Semaphore;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ReactiveJDBCConfiguration.class)

public class QueryStreamerJdbcTemplateTest {

    @Autowired
    DataSource dataSource;

@Test
public void testQueryTakeOnlyTheFirstTwo() throws InterruptedException {

    QueryStreamerJdbcTemplate template = new QueryStreamerJdbcTemplate(dataSource);

    Semaphore semaphore = new Semaphore(1);
    semaphore.acquire(1);
    Observable<User> userObservable = template.stream("select * from User",
            (resultSet, rowNumber) -> new User(resultSet.getInt("userID"), resultSet.getString("name"), resultSet.getInt("age")))
            .take(2);
    userObservable
           .subscribe(System.out::println,
                   System.out::println,
                   ()->{System.out.println("Stream completed.....");
                   semaphore.release(1);});

    semaphore.acquire(1);
    semaphore.release(1);


}

class MyDisposableObserver implements Disposable{

    @Override
    public void dispose() {

    }

    @Override
    public boolean isDisposed() {
        return false;
    }
}


}