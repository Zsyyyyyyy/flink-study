package com.zsy;


import com.zsy.constants.SyncConstant;
import com.zsy.utils.PropertiesConfigUtils;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.connectors.pulsar.FlinkPulsarSource;
import org.apache.flink.streaming.util.serialization.PulsarDeserializationSchema;
import org.apache.pulsar.client.api.SubscriptionType;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TestPulsar {
  public static void main(String[] args) throws Exception {
    //
      StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
      env.disableOperatorChaining();
      env.enableCheckpointing(60*1000*5, CheckpointingMode.EXACTLY_ONCE);
      env.getCheckpointConfig().setCheckpointTimeout(60000);
      env.setRestartStrategy(RestartStrategies.fixedDelayRestart(0,3*1000));

      ParameterTool parameters = ParameterTool.fromArgs(args);
      String envNameArg = parameters.get(SyncConstant.ENV_NAME_ARG);
      Properties config = PropertiesConfigUtils.getConfig(envNameArg);
      Configuration conf = new Configuration();
      config.forEach((k,v) -> conf.setString(k.toString(), v.toString()));

      // 新增收藏
      Properties propAdd = new Properties();
      propAdd.setProperty("topic", config.getProperty("pulsar.feed.topic"));
      propAdd.setProperty("partition.discovery.interval-millis",config.getProperty("partition.discovery.interval-millis"));
      propAdd.setProperty("pulsar.producer.subscriptionName", "data-warehouse");
      propAdd.setProperty("pulsar.producer.subscriptionType", String.valueOf(SubscriptionType.Shared));
      FlinkPulsarSource<String> pulsarSourceAdd = new FlinkPulsarSource<>(config.getProperty("pulsar.service-url"), config.getProperty("pulsar.admin-url"), PulsarDeserializationSchema.valueOnly(new SimpleStringSchema()), propAdd);
//      pulsarSourceAdd.setStartFromEarliest();
      pulsarSourceAdd.setStartFromLatest();
      DataStreamSource<String> dataStreamSourceAdd = env.addSource(pulsarSourceAdd);
      dataStreamSourceAdd.print();

//      StreamingFileSink<String> build = StreamingFileSink.<String>forRowFormat(new Path("./output")
//              , new SimpleStringEncoder<>("UTF-8"))
//              .withRollingPolicy(
//                      DefaultRollingPolicy.builder()
//                              .withRolloverInterval(TimeUnit.MINUTES.toMillis(15))
//                              .withInactivityInterval(TimeUnit.MINUTES.toMillis(5))
//                              .withMaxPartSize(1024 * 1024 * 1024)
//                              .build()).build();
//      DataStreamSink<String> stringDataStreamSink = dataStreamSourceAdd.addSink(build);

      env.execute();



  }
}