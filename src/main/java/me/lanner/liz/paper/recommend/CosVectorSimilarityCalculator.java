package me.lanner.liz.paper.recommend;

import me.lanner.liz.paper.math.Vector;
import sun.print.DocumentPropertiesUI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CosVectorSimilarityCalculator implements VectorSimilarityCalculator {
    @Override
    public String name() {
        return "cos";
    }

    @Override
    public Double calcSimilarity(Vector<Integer, Double> va, Vector<Integer, Double> vb) {
        Set<Integer> xyInterSet = new HashSet<>();
        double powerSum = 0.0D;
        for (Integer mId : va.allX()) {
            Double rate = va.get(mId);
            powerSum += Math.pow(rate, 2);
        }
        for (Integer mId : vb.allX()) {
            if (va.allX().contains(mId)) {
                xyInterSet.add(mId);
            }
            Double rate = vb.get(mId);
            powerSum += Math.pow(rate, 2);
        }
        double prodSum = 0.0D;
        for (Integer mId : xyInterSet) {
            Double r1 = va.get(mId);
            Double r2 = vb.get(mId);
            prodSum += r1*r2;
        }
        return prodSum / Math.sqrt(powerSum);
    }
}
