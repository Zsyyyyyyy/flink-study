package com.zsy.source;

import com.zsy.pojo.Event;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Calendar;
import java.util.Random;


public class CustomClickSource implements SourceFunction<Event> {

    private Boolean running = true;

    @Override
    public void run(SourceContext<Event> sourceContext) throws Exception {
        Random random = new Random();
        String[] users = {"Marry","Alice","Bob","Cary"};
        String[] urls = {"./home","./cart","./fav","./prod?id=1","./prod?id=2"};

        while (running) {
            sourceContext.collect(new Event(
                    users[random.nextInt(users.length)],
                    urls[random.nextInt(urls.length)],
                    Calendar.getInstance().getTimeInMillis()
            ));

            Thread.sleep(1000);


        }

    }



    @Override
    public void cancel() {
        running = false;
    }



}
