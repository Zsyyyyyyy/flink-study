//package com.zsy.watermark;
//
//import com.zsy.pojo.Event;
//import com.zsy.source.CustomClickSource;
//import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
//import org.apache.flink.api.common.eventtime.WatermarkGenerator;
//import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
//import org.apache.flink.api.common.eventtime.WatermarkStrategy;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//
//import java.time.Duration;
//
//public class WaterMarkTest1 {
//    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
//        DataStreamSource<Event> stream = env.addSource(new CustomClickSource());
//
//        // 有序流
//        stream.assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps()
//        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
//            @Override
//            public long extractTimestamp(Event event, long l) {
//                return event.timestamp;
//            }
//        }));
//
//        // 无序流
//        stream.assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(5))
//        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
//            @Override
//            public long extractTimestamp(Event event, long l) {
//                return event.timestamp;
//            }
//        }));
//
//        env.execute();
//    }
//}