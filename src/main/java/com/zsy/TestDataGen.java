package com.zsy;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TestDataGen {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
//设置WebUI绑定的本地端口
        conf.setString(RestOptions.BIND_PORT,"8081");
//使用配置
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setParallelism(1);
        DataGeneratorSource<String> dataGenSource = new DataGeneratorSource<>(
                new GeneratorFunction<Long, String>() {
                    @Override
                    public String map(Long aLong) throws Exception {
                        return "Value:" + aLong;
                    }
                }, Long.MAX_VALUE, RateLimiterStrategy.perSecond(10)
                , Types.STRING

        );
        DataStreamSource<String> ds = env.fromSource(dataGenSource, WatermarkStrategy.noWatermarks(), "dataGen");

        SingleOutputStreamOperator<Integer> map = ds.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String s) throws Exception {
                return 1;
            }
        });
        map.print();

        env.execute();


    }
}
