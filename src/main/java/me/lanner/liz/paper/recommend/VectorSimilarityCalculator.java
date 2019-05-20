package me.lanner.liz.paper.recommend;

import me.lanner.liz.paper.math.Vector;

public interface VectorSimilarityCalculator {

    String name();

    Double calcSimilarity(Vector<Integer, Double> va, Vector<Integer, Double> vb);

}
