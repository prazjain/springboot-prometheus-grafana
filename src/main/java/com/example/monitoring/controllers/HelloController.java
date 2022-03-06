package com.example.monitoring.controllers;

import com.example.monitoring.FailureCompartment;
import com.example.monitoring.TimedTask;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private List<String> botNames = Arrays.asList("Bot1", "Bot2", "Bot3", "Bot4");

    @Autowired
    MeterRegistry meterRegistry;

    /* Can also annotate, but while annotating we cannot pass dynamic values like tags consisting of method params
    @Timed(
        value="prash.rasa.requests",
        histogram = true,
        percentiles = {0.10, 0.50, 0.95, 0.99},
        extraTags = {"version", "1.0"})
    */
    @GetMapping("/rasa/{bot}")
    public ResponseEntity<String> rasa(@PathVariable String bot) {
        Counter counter = Counter.builder("prash.rasa.requests.errors")
                              .tag("bot", bot)
                              .description("Error count for rasa endpoint").register(meterRegistry);

        Timer.Builder timer = Timer.builder("prash.rasa.requests")
                    .sla(Duration.ofMillis(1), Duration.ofMillis(10))
                    .publishPercentileHistogram()
                    .tag("bot", bot);

        Function<ResponseEntity<String>, Tag> getStatus = (responseEntity) -> Tag.of("status", String.valueOf(responseEntity.getStatusCodeValue()));
        return FailureCompartment.<ResponseEntity<String>>run(() -> getRandomResult("Rasa"),getStatus, timer, counter, meterRegistry);
    }

    /*
    Can also annotate for Timer metrics
  @Timed(
      value = "prash.botformation.requests",
      histogram = true,
      percentiles = {0.10, 0.50, 0.95, 0.99},
      extraTags = {"version", "1.0"})
  */
  @GetMapping("/botformation/{bot}")
  public ResponseEntity<String> botformation(@PathVariable String bot) {
      Counter counter = Counter.builder("prash.botformation.requests.errors")
                            .tag("bot", bot)
                            .description("Error count for rasa endpoint").register(meterRegistry);

      Timer.Builder timer = Timer.builder("prash.botformation.requests")
                                //.sla(Duration.ofMillis(1), Duration.ofMillis(10))
                                .publishPercentileHistogram()
                                .tag("bot", bot);

      //random payload size in KBs
      int payloadSize = (new Random()).nextInt(10) + 1;
      DistributionSummary summary = DistributionSummary
                                        .builder("prash.botformation.requestpayload")
                                        .description("Metric for Botformation request payload size in KBs") // optional
                                        .baseUnit("kilobytes") // optional (1)
                                        .register(meterRegistry);
      summary.record(payloadSize);

      Function<ResponseEntity<String>, Tag> getStatus = (responseEntity) -> Tag.of("status", String.valueOf(responseEntity==null ? 500 : responseEntity.getStatusCodeValue()));
      ResponseEntity<String> result = FailureCompartment.<ResponseEntity<String>>run(() -> getRandomResult("Botformation"),getStatus, timer, counter, meterRegistry);
      return result;
  }

    private ResponseEntity<String> getRandomResult(String value) {
      //choose random response status
        int ran = (new Random()).nextInt(5) + 1;
        //add random latency
        try {
            Thread.sleep(((new Random()).nextInt(5) + 1) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        switch (ran) {
            case 1:
                return ResponseEntity.ok(value);
            case 2:
                return ResponseEntity.internalServerError().build();
            case 3:
                return ResponseEntity.notFound().build();
            case 4:
                return ResponseEntity.badRequest().build();
            default:
                throw new RuntimeException("Error!");
        }
    }
}
