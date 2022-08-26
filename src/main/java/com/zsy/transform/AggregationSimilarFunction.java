package com.zsy.transform;

import com.zsy.pojo.Event;
import com.zsy.source.CustomClickSource;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class AggregationSimilarFunction {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);

        DataStreamSource<Event> stream = env.addSource(new CustomClickSource());

        KeyedStream<Event, String> keyedStream = stream.keyBy(new KeySelector<Event, String>() {

            @Override
            public String getKey(Event event) throws Exception {
                return event.user;
            }
        });
        SingleOutputStreamOperator<Event> sum = keyedStream.minBy("timestamp");
        sum.print();


        env.execute();


    }
}
