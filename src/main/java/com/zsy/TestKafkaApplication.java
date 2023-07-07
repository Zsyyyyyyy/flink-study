package com.zsy;

import com.zsy.utils.PropertiesConfigUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.util.Properties;
import java.util.regex.Pattern;

import static com.zsy.constants.SyncConstant.ENV_NAME_ARG;
import static com.zsy.constants.SyncConstant.TOPIC_PATTERN_TEMPLATE;

/**
 * @auth: zsy
 * @date: 2022/12/6 10:42
 */
public class TestKafkaApplication {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String envNameArg = parameterTool.get(ENV_NAME_ARG);
        System.out.println(envNameArg);
        Properties customerProp = PropertiesConfigUtils.getConfig(envNameArg);

        Configuration flinkConf = new Configuration();
        customerProp.forEach((k, v) -> flinkConf.setString(k.toString(), v.toString()));
        env.getConfig().setGlobalJobParameters(flinkConf);

        Properties prop = new Properties();
        prop.setProperty("enable.auto.commit", "true");
        String envNam = customerProp.getProperty(ENV_NAME_ARG);
        String format = String.format(TOPIC_PATTERN_TEMPLATE, envNam);
        System.out.println(Pattern.compile(format));
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers(customerProp.getProperty("kafka.bootstrapServers"))
                .setTopicPattern(Pattern.compile(format))
//                .setTopics("topic-dev02-bi-databricks-app")
                .setGroupId(customerProp.getProperty("kafka.groupId"))
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.LATEST))
                .setProperties(prop)
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        DataStreamSource<String> kafkaSourceDS = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka source");
        kafkaSourceDS.print();
        env.execute();


    }
}
