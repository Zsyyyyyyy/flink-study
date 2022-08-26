package com.zsy.transform;

import com.zsy.pojo.Event;
import com.zsy.source.CustomClickSource;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class RichMapFunctionTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        DataStreamSource<Event> stream = env.addSource(new CustomClickSource());
        SingleOutputStreamOperator<String> map = stream.map(new RichMapFunction<Event, String>() {
            @Override
            public void open(Configuration parameters) throws Exception {
                System.out.println("开始啦!!!");
            }

            @Override
            public String map(Event event) throws Exception {
                return event.user;
            }

            @Override
            public void close() throws Exception {
                System.out.println("结束了!!!");
            }
        });
        map.print();

        env.execute();
    }
}
