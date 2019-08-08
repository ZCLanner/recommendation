package me.lanner.liz.paper.attack;

import me.lanner.liz.paper.math.Matrix;
import me.lanner.liz.paper.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Profile("attack")
@ConditionalOnProperty(value = "attack.algo.enable.avg", havingValue = "true")
public class AvgAttackDataGenerator extends AbstractAttackDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvgAttackDataGenerator.class);

    private Map<Integer, Double> movieAvgRateMap;
    private Map<Integer, Integer> movieIdRangeMap;
    private Random random = new Random(System.currentTimeMillis());

    @Override
    public void analyze(Matrix<Integer> sourceData) {
        super.analyze(sourceData);
        Map<Integer, Integer> ratedCountMap = new HashMap<>();
        Map<Integer, Double> rateSumMap = new HashMap<>();
        int totalCount = 0;
        for (final Integer userId : sourceData.allX()) {
            Vector<Integer, Double> userRatings = sourceData.get(userId);
            for (Integer movieId : userRatings.allX()) {
                Integer ratedCount = ratedCountMap.computeIfAbsent(movieId, mid -> 0);
                ratedCount = ratedCount+1;
                ratedCountMap.put(movieId, ratedCount);
                Double rateSum = rateSumMap.computeIfAbsent(movieId, mid -> 0.0D);
                rateSum = rateSum + userRatings.get(movieId);
                rateSumMap.put(movieId, rateSum);
                totalCount++;
            }
        }
        movieAvgRateMap = new HashMap<>(ratedCountMap.size());
        movieIdRangeMap = new HashMap<>(totalCount);
        int i = 0;
        for (Integer movieId : ratedCountMap.keySet()) {
            Integer count = ratedCountMap.get(movieId);
            Double rateSum = rateSumMap.get(movieId);
            Double avg = rateSum/count;
            movieAvgRateMap.put(movieId, avg);
            for (int j = 0; j < count; j++, i++) {
                movieIdRangeMap.put(i, movieId);
            }
        }
    }

    @Override
    protected Collection<Integer> movies() {
        Set<Integer> movieIds = new HashSet<>(attackRatingCount);
        while (movieIds.size() < attackRatingCount) {
            int rand = random.nextInt(movieIdRangeMap.size());
            Integer nextMovieId = movieIdRangeMap.get(rand);
            if (nextMovieId.equals(targetItem)) {
                continue;
            }
            movieIds.add(movieIdRangeMap.get(rand));
        }
        LOGGER.debug("AvgAttackDataGenerator generate {} attack movies, include target items ? {}", movieIds.size(), movieIds.contains(targetItem));
        return movieIds;
    }

    @Override
    protected Double rate(Integer movieId) {
        return movieAvgRateMap.get(movieId);
    }

    @Override
    public String strategy() {
        return "avg";
    }

}
