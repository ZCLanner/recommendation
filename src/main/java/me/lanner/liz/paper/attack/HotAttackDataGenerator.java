package me.lanner.liz.paper.attack;

import com.google.gson.Gson;
import me.lanner.liz.paper.math.Matrix;
import me.lanner.liz.paper.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Profile("hot")
public class HotAttackDataGenerator extends AbstractAttackDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotAttackDataGenerator.class);

    private Map<Integer, Double> hotMovieRateMap;

    @Value("${attack.hot.num}")
    private int hotMovieCount;

    @Override
    public void analyze(Matrix sourceData) {
        super.analyze(sourceData);
        hotMovieRateMap = new HashMap<>(hotMovieCount);
        Map<Integer, Integer> ratedCountMap = new HashMap<>();
        Map<Integer, Double> rateSumMap = new HashMap<>();
        for (final Integer userId : sourceData.allX()) {
            Vector<Double> userRatings = sourceData.get(userId);
            for (Integer movieId : userRatings.allX()) {
                Integer ratedCount = ratedCountMap.computeIfAbsent(movieId, mid -> 0);
                ratedCount = ratedCount+1;
                ratedCountMap.put(movieId, ratedCount);
                Double rateSum = rateSumMap.computeIfAbsent(movieId, mid -> 0.0D);
                rateSum = rateSum + userRatings.get(movieId);
                rateSumMap.put(movieId, rateSum);
            }
        }
        List<Map.Entry<Integer, Integer>> ratedCountSortedMovieIds = new ArrayList<>(ratedCountMap.entrySet());
        ratedCountSortedMovieIds.sort(Comparator.comparing(Map.Entry::getValue));
        for (int i = 0; i < hotMovieCount && i < ratedCountSortedMovieIds.size(); i++) {
            Map.Entry<Integer, Integer> entry = ratedCountSortedMovieIds.get(i);
            Double rateSum = rateSumMap.get(entry.getKey());
            Double avgRate = Math.ceil(rateSum/entry.getValue());
            hotMovieRateMap.put(entry.getKey(), avgRate);
        }
        LOGGER.info("Done analyze hot movies|HotMovieAvgRate={}", new Gson().toJson(hotMovieRateMap));
    }

    @Override
    protected Collection<Integer> movies() {
        List<Integer> movieIds = new ArrayList<>(hotMovieRateMap.keySet());
        Collections.shuffle(movieIds);
        return movieIds.subList(0, attackRatingCount);
    }

    @Override
    protected Double rate(Integer movieId) {
        return hotMovieRateMap.get(movieId);
    }

    @Override
    public String strategy() {
        return "hot";
    }

}
