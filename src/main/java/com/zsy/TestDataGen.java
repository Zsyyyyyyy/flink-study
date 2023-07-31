package com.zsy;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

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
//                        return  aLong.toString();
                    }
                }, Long.MAX_VALUE, RateLimiterStrategy.perSecond(10)
                , Types.STRING

        );
        DataStreamSource<String> ds = env.fromSource(dataGenSource, WatermarkStrategy.noWatermarks(), "dataGen");

        SingleOutputStreamOperator<Tuple2<String, Integer>> map = ds.map(data -> new Tuple2<String, Integer>(data, 1)).returns(Types.TUPLE(Types.STRING, Types.INT));

//        KeyedStream<Integer, Integer> stringStringKeyedStream = map.keyBy(e -> e);
        KeyedStream<Tuple2<String, Integer>, String> tuple2StringKeyedStream = map.keyBy(data -> "1");

        SingleOutputStreamOperator<String> reduce = tuple2StringKeyedStream.countWindow(3, 1).aggregate(new AggregateFunction<Tuple2<String, Integer>, Integer, String>() {
            @Override
            public Integer createAccumulator() {
                System.out.println("触发累加器");
                return 0;
            }

            @Override
            public Integer add(Tuple2<String, Integer> t1, Integer integer) {
                System.out.println("调用add方法");

                return integer + t1.f1;
            }

            @Override
            public String getResult(Integer integer) {
                System.out.println("调用result方法");
                return integer.toString();
            }

            @Override
            public Integer merge(Integer integer, Integer acc1) {
                System.out.println("调用merge方法");
                return null;
            }
        });

//        SingleOutputStreamOperator<Integer> sum = stringStringKeyedStream.sum(0);
//        stringStringKeyedStream.print();

//        map.print();
//        tuple2StringKeyedStream.print();
        reduce.print();
        env.execute();




    }
}
