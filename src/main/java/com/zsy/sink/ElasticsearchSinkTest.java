package com.zsy.sink;

import com.zsy.pojo.Event;
import com.zsy.source.CustomClickSource;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.util.ArrayList;
import java.util.HashMap;

public class ElasticsearchSinkTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        DataStreamSource<Event> stream = env.addSource(new CustomClickSource());

        ArrayList<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost("hadoop102", 9200, "http"));

        ElasticsearchSinkFunction<Event> eventElasticsearchSinkFunction = new ElasticsearchSinkFunction<Event>() {

            @Override
            public void process(Event event, RuntimeContext runtimeContext, RequestIndexer requestIndexer) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(event.user, event.url);

                IndexRequest request = Requests.indexRequest()
                        .index("clicks")
                        .source(hashMap);

                requestIndexer.add(request);
            }
        };

        stream.addSink(new ElasticsearchSink.Builder<Event>(httpHosts, eventElasticsearchSinkFunction).build());

        env.execute();

    }
}
