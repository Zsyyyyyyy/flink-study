package com.zsy.sink;

import com.zsy.pojo.Event;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

public class MyRedisMapper implements RedisMapper<Event> {

    @Override
    public RedisCommandDescription getCommandDescription() {
        return new RedisCommandDescription(RedisCommand.HSET, "clicks");
    }

    @Override
    public String getKeyFromData(Event event) {
        return event.user;
    }

    @Override
    public String getValueFromData(Event event) {
        return event.url;
    }
}
