//package com.zsy.window;
//
//import com.zsy.pojo.Event;
//import com.zsy.source.CustomClickSource;
//import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
//import org.apache.flink.api.common.eventtime.WatermarkStrategy;
//import org.apache.flink.api.common.functions.AggregateFunction;
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.streaming.api.datastream.KeyedStream;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.datastream.WindowedStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
//import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
//import org.apache.flink.streaming.api.windowing.time.Time;
//import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
//import org.apache.flink.util.Collector;
//
//import java.util.HashSet;
//
///**
// * 增量聚合函数和全窗口函数的结合使用
// */
//public class WindowFunctionIncrementalTest3 {
//    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
//        SingleOutputStreamOperator<Event> eventSingleOutputStreamOperator = env.addSource(new CustomClickSource())
//                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps()
//                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
//                            @Override
//                            public long extractTimestamp(Event event, long l) {
//                                return event.timestamp;
//                            }
//                        }));
//
//        KeyedStream<Event, String> keyedStream = eventSingleOutputStreamOperator.keyBy(r -> r.url);
//
//
//
//
//        //滚动事件时间窗口
//        WindowedStream<Event, String, TimeWindow> window = keyedStream.window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)));
//
//        window.aggregate(new UrlViewCountAgg(), new UrlViewCountResult()).print();
//
//
//
//        env.execute();
//    }
//    public static class UrlViewCountAgg implements AggregateFunction<Event, Long, Long> {
//
//
//        @Override
//        public Long createAccumulator() {
//            return 0L;
//        }
//
//        @Override
//        public Long add(Event event, Long aLong) {
//            return aLong + 1;
//        }
//
//        @Override
//        public Long getResult(Long aLong) {
//            return aLong;
//        }
//
//        @Override
//        public Long merge(Long aLong, Long acc1) {
//            return null;
//        }
//    }
//
//    public static class UrlViewCountResult extends ProcessWindowFunction<Long, UrlViewCount, String, TimeWindow> {
//
//        @Override
//        public void process(String s, Context context, Iterable<Long> iterable, Collector<UrlViewCount> collector) throws Exception {
//            Long start = context.window().getStart();
//            Long end = context.window().getEnd();
//
//            collector.collect(new UrlViewCount(s, iterable.iterator().next(), start, end));
//
//        }
//    }
//
//
//}
//
//
