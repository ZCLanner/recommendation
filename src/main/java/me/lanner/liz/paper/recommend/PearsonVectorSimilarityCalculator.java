package me.lanner.liz.paper.recommend;

import me.lanner.liz.paper.math.Vector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("rcmd")
@ConditionalOnProperty(value = "rcmd.algo.enable.pearson", havingValue = "true")
public class PearsonVectorSimilarityCalculator implements VectorSimilarityCalculator {
    @Override
    public String name() {
        return "pearson";
    }

    @Override
    public Double calcSimilarity(Vector<Integer, Double> va, Vector<Integer, Double> vb) {
        Double sumA = 0.0D, sumB = 0.0D;
        for (Integer mId : va.allX()) {
            sumA += va.get(mId);
        }
        for (Integer mId : vb.allX()) {
            sumB += vb.get(mId);
        }
        Double avgA = sumA/va.length(), avgB = sumB/vb.length();

        Set<Integer> xyInterSet = new HashSet<>();
        double powerSum = 0.0D;
        for (Integer mId : va.allX()) {
            Double rate = va.get(mId);
            powerSum += Math.pow(rate-avgA, 2);
        }
        for (Integer mId : vb.allX()) {
            if (va.allX().contains(mId)) {
                xyInterSet.add(mId);
            }
            Double rate = vb.get(mId);
            powerSum += Math.pow(rate-avgB, 2);
        }
        double prodSum = 0.0D;
        for (Integer mId : xyInterSet) {
            Double r1 = va.get(mId);
            Double r2 = vb.get(mId);
            prodSum += (r1-avgA) * (r2-avgB);
        }
        return prodSum / Math.sqrt(powerSum);
    }
}
