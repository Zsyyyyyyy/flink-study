//package com.zsy.window;
//
//import com.zsy.pojo.Event;
//import com.zsy.source.CustomClickSource;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.datastream.KeyedStream;
//import org.apache.flink.streaming.api.datastream.WindowedStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.windowing.assigners.*;
//import org.apache.flink.streaming.api.windowing.time.Time;
//import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
//import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
//
//public class WindowTest1 {
//    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
//        DataStreamSource<Event> stream = env.addSource(new CustomClickSource());
//
//        KeyedStream<Event, String> keyedStream = stream.keyBy(r -> r.user);
//
//        //滚动事件时间窗口
//        WindowedStream<Event, String, TimeWindow> window1 = keyedStream.window(TumblingEventTimeWindows.of(Time.days(1), Time.hours(-8)));
//        //滑动事件时间窗口
//        WindowedStream<Event, String, TimeWindow> window2 = keyedStream.window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5), Time.hours(-8)));
//
//        //会话窗口，静态时长
//        WindowedStream<Event, String, TimeWindow> window3 = keyedStream.window(EventTimeSessionWindows.withGap(Time.seconds(10)));
//        //会话窗口，动态时长
//        WindowedStream<Event, String, TimeWindow> window4 = keyedStream.window(EventTimeSessionWindows.withDynamicGap(new SessionWindowTimeGapExtractor<Event>() {
//            @Override
//            public long extract(Event event) {
//                return event.timestamp + 1000;
//            }
//        }));
//        //滚动计数窗口
//        WindowedStream<Event, String, GlobalWindow> eventStringGlobalWindowWindowedStream = keyedStream.countWindow(10);
//        //滑动计数窗口
//        WindowedStream<Event, String, GlobalWindow> eventStringGlobalWindowWindowedStream1 = keyedStream.countWindow(10,6);
//
//        keyedStream.window(GlobalWindows.create());
//
//
//        env.execute();
//    }
//}
