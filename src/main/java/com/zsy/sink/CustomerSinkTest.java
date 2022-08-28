package com.zsy.sink;

import com.zsy.pojo.Event;
import com.zsy.source.CustomClickSource;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;

import java.nio.charset.StandardCharsets;

public class CustomerSinkTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        DataStreamSource<Event> stream = env.addSource(new CustomClickSource());

        stream.addSink(new RichSinkFunction<Event>() {
            public org.apache.hadoop.conf.Configuration configuration;

            public Connection connection;

            @Override
            public void open(Configuration parameters) throws Exception {
                configuration = HBaseConfiguration.create();
                configuration.set("hbase.zookeeper.quorum","hadoop102:2181");
                connection = ConnectionFactory.createConnection(configuration);

            }

            @Override
            public void invoke(Event value, Context context) throws Exception {
                Table table = connection.getTable(TableName.valueOf("test"));
                Put put = new Put("rowkey".getBytes(StandardCharsets.UTF_8));
                put.addColumn("info".getBytes(StandardCharsets.UTF_8)
                        ,value.user.getBytes(StandardCharsets.UTF_8)
                        ,"1".getBytes(StandardCharsets.UTF_8)
                        );
                table.put(put);
                table.close();
            }

            @Override
            public void close() throws Exception {

                connection.close();
            }
        });

        env.execute();
    }
}
