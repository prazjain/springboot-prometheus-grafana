package com.example.monitoring;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class TimedTask {

    public static <T> Pair<Long, T> run(Callable<T> task) {
        T call = null;
        Instant start = Instant.now();
        try {
            call = task.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long millis = Duration.between(start, Instant.now()).toMillis();
        return new ImmutablePair<>(millis, call);
    }
}
