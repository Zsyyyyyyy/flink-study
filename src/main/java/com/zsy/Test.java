//package com.zsy;
//
//import org.apache.flink.api.common.eventtime.WatermarkStrategy;
//import org.apache.flink.api.common.serialization.SimpleStringSchema;
//import org.apache.flink.connector.kafka.source.KafkaSource;
//import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//
////import org.apache.flink.api.java.ExecutionEnvironment;
////import org.apache.flink.api.java.operators.DataSource;
////import org.apache.flink.api.java.typeutils.RowTypeInfo;
////import org.apache.flink.connector.jdbc.JdbcInputFormat;
////import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
////import org.apache.flink.types.Row;
////
/////**
//// * @auth: zsy
//// * @date: 2023/2/2 17:38
//// */
//
///**
// * cd /root/WORK/data-warehouse && python3 search/db2kafkaTest.py --env test --db_type mongodb --db_name platform_feed_management --tbl_name feed --key source --value USER
// */
//
//public class Test {
//    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        KafkaSource<String> source = KafkaSource.<String>builder()
//                .setBootstrapServers("test01:9092")
//                .setTopics("zsy_test")
//                .setGroupId("my-group")
//                .setStartingOffsets(OffsetsInitializer.earliest())
//                .setValueOnlyDeserializer(new SimpleStringSchema())
//                .build();
//
//        DataStreamSource<String> kafkaSource = env.fromSource(source, WatermarkStrategy.noWatermarks(), "kafka source");
//        kafkaSource.print();
//
//
//        env.execute();
//    }
//}
//
////public class Test {
////    public static void main(String[] args) throws Exception {
////        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
////        DataSource<Row> input = env.createInput(JdbcInputFormat.buildJdbcInputFormat()
////                .setDrivername("com.mysql.cj.jdbc.Driver")
////                .setDBUrl("jdbc:mysql://localhost:3306/ssm")
////                .setUsername("root")
////                .setPassword("12345678")
////                .setQuery("select * from t_user")
////                .setRowTypeInfo(new RowTypeInfo(BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO))
////                .finish());
////
////        input.print();
////
////        env.execute();
////
////
////
////
////
////    }
////}
