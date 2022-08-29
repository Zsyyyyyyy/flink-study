package com.zsy.window;

import com.zsy.pojo.Event;
import com.zsy.source.CustomClickSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.util.HashSet;

/**
 * 10s的人均pv
 */
public class WindowFunctionIncrementalTest2 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        SingleOutputStreamOperator<Event> eventSingleOutputStreamOperator = env.addSource(new CustomClickSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event event, long l) {
                                return event.timestamp;
                            }
                        }));

        KeyedStream<Event, Boolean> keyedStream = eventSingleOutputStreamOperator.keyBy(r -> true);


        //滚动事件时间窗口
        WindowedStream<Event, Boolean, TimeWindow> window = keyedStream.window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(2)));

        window.aggregate(new AvgPv()).print();



        env.execute();
    }
    public static class AvgPv implements AggregateFunction<Event, Tuple2<HashSet<String>, Long>, Double> {

        @Override
        public Tuple2<HashSet<String>, Long> createAccumulator() {
            return Tuple2.of(new HashSet<String>(), 0L);
        }

        @Override
        public Tuple2<HashSet<String>, Long> add(Event event, Tuple2<HashSet<String>, Long> hashSetLongTuple2) {
            hashSetLongTuple2.f0.add(event.user);
            return Tuple2.of(hashSetLongTuple2.f0, hashSetLongTuple2.f1 + 1L);
        }

        @Override
        public Double getResult(Tuple2<HashSet<String>, Long> hashSetLongTuple2) {
            return (double) hashSetLongTuple2.f1 / hashSetLongTuple2.f0.size();
        }

        @Override
        public Tuple2<HashSet<String>, Long> merge(Tuple2<HashSet<String>, Long> hashSetLongTuple2, Tuple2<HashSet<String>, Long> acc1) {
            return null;
        }
    }


}


