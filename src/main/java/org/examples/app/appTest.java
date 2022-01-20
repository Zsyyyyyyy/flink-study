package org.examples.app;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class appTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment()
                .setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        DataStreamSource<String> data = env.socketTextStream("localhost", 9999);
        SingleOutputStreamOperator<String> data1 = data.map(new MapFunction<String, String>() {
            @Override
            public String map(String s) throws Exception {
                return s + "_";
            }
        });
        data1.print();
        env.execute();

    }
}
