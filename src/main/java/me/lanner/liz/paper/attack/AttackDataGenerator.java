package me.lanner.liz.paper.attack;

import me.lanner.liz.paper.math.Matrix;

public interface AttackDataGenerator {

    default void analyze(Matrix<Integer> sourceData) { }

    String strategy();

    Matrix<Integer> generate(Matrix<Integer> sourceData, Integer scalePercent);

}
