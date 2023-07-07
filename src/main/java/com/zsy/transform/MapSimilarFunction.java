//package com.zsy.transform;
//
//import com.zsy.pojo.Event;
//import com.zsy.source.CustomClickSource;
//import org.apache.flink.api.common.functions.FilterFunction;
//import org.apache.flink.api.common.functions.FlatMapFunction;
//import org.apache.flink.api.common.functions.MapFunction;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.util.Collector;
//
//public class MapSimilarFunction {
//
//        public static void main(String[] args) throws Exception {
//            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
////        Properties prop = new Properties();
////        prop.setProperty("bootstrap.servers","centos111");
////        prop.setProperty("group.id","consumer-group");
////        prop.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
////        prop.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
////        prop.setProperty("auto.offset.reset","latest");
////
////        DataStreamSource<String> stream = env.addSource(new FlinkKafkaConsumer<String>(
////                "clicks",
////                new SimpleStringSchema(),
////                prop
////        ));
////
////        stream.print("kafka");
////        env.execute();
//
//            DataStreamSource<Event> stream = env.addSource(new CustomClickSource());
//
//            SingleOutputStreamOperator<Event> marry = stream.filter(new FilterFunction<Event>() {
//                @Override
//                public boolean filter(Event event) throws Exception {
//                    return event.user.equals("Marry");
//                }
//            });
//
//            SingleOutputStreamOperator<String> map = stream.map(new MapFunction<Event, String>() {
//                @Override
//                public String map(Event event) throws Exception {
//                    return event.url;
//                }
//            });
//
//            SingleOutputStreamOperator<String> stringSingleOutputStreamOperator = stream.flatMap(new FlatMapFunction<Event, String>() {
//                @Override
//                public void flatMap(Event event, Collector<String> collector) throws Exception {
//                    if (event.user.equals("Marry")) {
//                        collector.collect(event.user);
//                    } else if (event.user.equals("Bob")) {
//                        collector.collect(event.user);
//                        collector.collect(event.url);
//                    }
//                }
//            });
//
//            stringSingleOutputStreamOperator.print("customSource");
//            env.execute();
//        }
//
//}
