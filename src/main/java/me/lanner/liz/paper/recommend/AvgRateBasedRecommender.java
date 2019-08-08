package me.lanner.liz.paper.recommend;

import me.lanner.liz.paper.math.Matrix;
import me.lanner.liz.paper.math.Vector;
import me.lanner.liz.paper.utils.SortUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

public class AvgRateBasedRecommender<U> implements Recommender<U> {

    @Value("${rcmd.movie.count}")
    private Integer recommendedMovieCount;

    @Override
    public Set<Integer> recommend(Matrix<U> matrix, Set<U> similarUsers) {
        Map<Integer, Double> movieRateSumMap = new HashMap<>();
        Map<Integer, Integer> movieRateCountMap = new HashMap<>();
        Map<Integer, Double> movieRateAvgMap = new HashMap<>();
        for (U similarUser : similarUsers) {
            Vector<Integer, Double> rateVector = matrix.get(similarUser);
            for (Integer movieId : rateVector.allX()) {
                Double rate = rateVector.get(movieId);
                Double rateSum = movieRateSumMap.computeIfAbsent(movieId, mId -> 0.0D);
                rateSum += rate;
                movieRateSumMap.put(movieId, rateSum);
                Integer rateCount = movieRateCountMap.computeIfAbsent(movieId, mId -> 0);
                rateCount++;
                movieRateCountMap.put(movieId, rateCount);
            }
            for (Integer movieId : movieRateCountMap.keySet()) {
                Double sum = movieRateSumMap.get(movieId);
                Integer count = movieRateCountMap.get(movieId);
                Double avg = sum / count;
                movieRateAvgMap.put(movieId, avg);
            }
        }
        List<Map.Entry<Integer, Double>> avgMovieRates = SortUtils.sortByValueDesc(movieRateAvgMap);
        Set<Integer> movieIds = new HashSet<>();
        for (int i = 0; i < recommendedMovieCount && i < avgMovieRates.size(); i++) {
            movieIds.add(avgMovieRates.get(i).getKey());
        }
        return movieIds;
    }
}
