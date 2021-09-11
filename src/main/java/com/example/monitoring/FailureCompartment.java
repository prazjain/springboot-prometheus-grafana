package com.example.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.ResponseEntity;

public class FailureCompartment {
    public static <T> T run(Callable<T> task, Function<T, Tag> timerTag, Timer.Builder timer, Counter errorCounter, MeterRegistry meterRegistry) {
        T call = null;
        Instant start = Instant.now();
        try {
            call = task.call();
            Tag tag = timerTag.apply(call);
            timer.tag(tag.getKey(), tag.getValue());
        } catch (Throwable e) {
            errorCounter.increment();
            e.printStackTrace();
        }
        long millis = Duration.between(start, Instant.now()).toMillis();
        timer.register(meterRegistry).record(millis, TimeUnit.MILLISECONDS);
        return call;
    }
}
