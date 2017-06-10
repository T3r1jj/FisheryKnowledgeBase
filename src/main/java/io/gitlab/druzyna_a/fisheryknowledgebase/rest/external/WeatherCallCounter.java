package io.gitlab.druzyna_a.fisheryknowledgebase.rest.external;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Damian Terlecki
 */
@Component
@Scope("singleton")
public class WeatherCallCounter {

    private List<Long> callTimes = new LinkedList<>();

    public boolean canCall() {
        refreshCallTimes();
        return callTimes.size() <= OpenWeatherService.CALL_PER_MIN_LIMIT;
    }

    public void call() {
        callTimes.add(Instant.now().getEpochSecond());
    }

    private void refreshCallTimes() {
        final long epochSecond = Instant.now().getEpochSecond();
        callTimes = callTimes.stream().filter(t -> t >= epochSecond - 60).collect(Collectors.toList());
    }

}
