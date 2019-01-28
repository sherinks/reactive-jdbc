package com.sks.reactive.reactivejdbc.jdbc.streaming;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryStreamerJdbcTemplate extends JdbcTemplate {


    private static Logger logger = LoggerFactory.getLogger(QueryStreamerJdbcTemplate.class);

    QueryStreamerJdbcTemplate(DataSource dataSource){
        setDataSource(dataSource);
        afterPropertiesSet();
    }

    public <T> Observable<T> stream(final String sql, RowMapper<T> rowMapper) throws DataAccessException {

        Assert.notNull(sql, "SQL must not be null");
        Assert.notNull(rowMapper, "RowMapper must not be null");
        if (logger.isDebugEnabled()) {
            logger.debug("Executing SQL query [" + sql + "]");
        }

        return Observable.using(() -> {
                    Connection con = DataSourceUtils.getConnection(obtainDataSource());
                    Statement statement = con.createStatement();
                    applyStatementSettings(statement);
                    ResultSet resultSet = statement.executeQuery(sql);
                    QueryConnectionSubscription connectionSubscription = new QueryConnectionSubscription(con, statement, resultSet);
                    return connectionSubscription;
                },
                (connectionSubscription) -> Observable.create((subscriber) -> {

                    ResultSet resultSet = connectionSubscription.getResultSet();
                    int rowNumber = 0;
                    while (resultSet.next() && !subscriber.isDisposed()) {

                        T row = rowMapper.mapRow(resultSet, rowNumber);
                        subscriber.onNext(row);
                    }
                    subscriber.onComplete();

                }), (queryConnectionSubscription) -> {
                    queryConnectionSubscription.close();
                });


    }

    class QueryConnectionSubscription {

        private Connection connection;

        private Statement statement;

        private ResultSet resultSet;


        public QueryConnectionSubscription(Connection connection, Statement statement, ResultSet resultSet) {
            this.connection = connection;
            this.statement = statement;
            this.resultSet = resultSet;
        }

        public Connection getConnection() {
            return connection;
        }

        public Statement getStatement() {
            return statement;
        }

        public ResultSet getResultSet() {
            return resultSet;
        }


        public void close() {

            logger.info("....Closing resources....");
            JdbcUtils.closeResultSet(getResultSet());
            JdbcUtils.closeStatement(getStatement());

            DataSourceUtils.releaseConnection(getConnection(), QueryStreamerJdbcTemplate.this.getDataSource());


        }

    }
}
