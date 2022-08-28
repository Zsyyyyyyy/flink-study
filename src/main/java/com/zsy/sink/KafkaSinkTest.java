package com.zsy.sink;

import com.zsy.pojo.Event;
import com.zsy.source.CustomClickSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

public class KafkaSinkTest {


    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        DataStreamSource<Event> stream = env.addSource(new CustomClickSource());
        Properties prop = new Properties();
        prop.put("bootstrap.servers","hadoop102:9092");

        SingleOutputStreamOperator<String> map = stream.map(new MapFunction<Event, String>() {
            @Override
            public String map(Event event) throws Exception {
                return event.toString();
            }
        });

        DataStreamSink<String> clicks = map.addSink(new FlinkKafkaProducer<String>(
                "clicks",
                new SimpleStringSchema(),
                prop
        ));



        env.execute();
    }
}
