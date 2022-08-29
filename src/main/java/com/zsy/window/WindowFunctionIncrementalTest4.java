package com.zsy.window;

import com.zsy.pojo.Event;
import com.zsy.source.CustomClickSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * 测试水位线和窗口函数的使用
 * Alice, ./home, 1000
 * Alice, ./cart, 2000
 * Alice, ./prod?id=100, 10000
 * Alice, ./prod?id=200, 8000
 * Alice, ./prod?id=300, 15000
 */
public class WindowFunctionIncrementalTest4 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);

        DataStreamSource<String> stream = env.socketTextStream("127.0.0.1", 7777);

        SingleOutputStreamOperator<Event> map = stream.map(new MapFunction<String, Event>() {
            @Override
            public Event map(String s) throws Exception {
                String[] split = s.split(",");
                return new Event(split[0].trim(), split[1].trim(), Long.valueOf(split[2].trim()));
            }
        });

        SingleOutputStreamOperator<Event> eventSingleOutputStreamOperator = map.assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(5)).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
            @Override
            public long extractTimestamp(Event event, long l) {
                return event.timestamp;
            }
        }));

        SingleOutputStreamOperator<String> process = eventSingleOutputStreamOperator.keyBy(data -> data.user).window(TumblingEventTimeWindows.of(Time.seconds(10))).process(new WatermarkTestResult());
        process.print();
    }

    public static class WatermarkTestResult extends ProcessWindowFunction<Event, String, String, TimeWindow> {

        @Override
        public void process(String s, Context context, Iterable<Event> iterable, Collector<String> collector) throws Exception {
            Long start = context.window().getStart();
            Long end = context.window().getEnd();
            Long l = context.currentWatermark();

            Long count = iterable.spliterator().getExactSizeIfKnown();

            collector.collect("窗口" + start + "~" +end + "中共有" + count + "个元素, 窗口闭合计算时, 水位线位于：" + l);
        }
    }
}


