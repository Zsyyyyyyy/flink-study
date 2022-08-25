package com.zsy.source;

import com.zsy.pojo.Event;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class KafkaSource {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
//        Properties prop = new Properties();
//        prop.setProperty("bootstrap.servers","centos111");
//        prop.setProperty("group.id","consumer-group");
//        prop.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
//        prop.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
//        prop.setProperty("auto.offset.reset","latest");
//
//        DataStreamSource<String> stream = env.addSource(new FlinkKafkaConsumer<String>(
//                "clicks",
//                new SimpleStringSchema(),
//                prop
//        ));
//
//        stream.print("kafka");
//        env.execute();

        DataStreamSource<Event> stream = env.addSource(new CustomClickSource());
        stream.print("customSource");
        env.execute();
    }
}
