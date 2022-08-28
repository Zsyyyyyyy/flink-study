package com.zsy.sink;

import com.zsy.pojo.Event;
import com.zsy.source.CustomClickSource;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JDBCSinkTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        DataStreamSource<Event> stream = env.addSource(new CustomClickSource());

        stream.addSink(JdbcSink.sink(
                "",
                (statement,r) -> {
                    statement.setString(1, r.user);
                    statement.setString(2,r.url);
                },
                JdbcExecutionOptions.builder()
                        .withBatchSize(1000)
                        .withBatchIntervalMs(200)
                        .withMaxRetries(5).build()
                ,
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl("jdbc:mysql://localhost:3306/test")
                .withDriverName("com.mysql.cj.jdbc.Driver")
                .withUsername("")
                .withPassword("")
                .build()
        ));

        env.execute();
    }
}
