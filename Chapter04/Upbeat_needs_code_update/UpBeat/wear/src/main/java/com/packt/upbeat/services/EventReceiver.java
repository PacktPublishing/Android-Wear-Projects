package com.packt.upbeat.services;

import com.packt.upbeat.utils.Reporter;

import java.util.concurrent.*;

/**
 * Created by ashok.kumar on 20/05/17.
 */

public class EventReceiver {
    private static ConcurrentMap<Class<?>, ConcurrentMap<Reporter, String>> events
            = new ConcurrentHashMap<Class<?>, ConcurrentMap<Reporter, String>>();

    private static BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
    private static ExecutorService executorService = new ThreadPoolExecutor(1, 10, 30, TimeUnit.SECONDS, queue);

    public static void register(Class<?> event, Reporter reporter) {
        if (null == event || null == reporter)
            return;

        events.putIfAbsent(event, new ConcurrentHashMap<Reporter, String>());
        events.get(event).putIfAbsent(reporter, "");
    }

    public static void remove(Class<?> event, Reporter reporter) {
        if (null == event || null == reporter)
            return;

        if (!events.containsKey(event))
            return;

        events.get(event).remove(reporter);
    }

    public static void notify(final Object event) {
        if (null == event)
            return;

        if (!events.containsKey(event.getClass()))
            return;

        for (final Reporter m : events.get(event.getClass()).keySet()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    m.notifyEvent(event);
                }
            });
        }
    }

}