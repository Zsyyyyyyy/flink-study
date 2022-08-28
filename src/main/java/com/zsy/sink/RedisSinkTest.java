package com.zsy.sink;

import com.zsy.pojo.Event;
import com.zsy.source.CustomClickSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;

public class RedisSinkTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        DataStreamSource<Event> stream = env.addSource(new CustomClickSource());
        SingleOutputStreamOperator<String> map = stream.map(new MapFunction<Event, String>() {

            @Override
            public String map(Event event) throws Exception {
                return event.toString();
            }
        });

        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("hadoop102").build();


        DataStreamSink<Event> eventDataStreamSink = stream.addSink(new RedisSink<Event>(conf, new MyRedisMapper()));
        env.execute();
    }
}
