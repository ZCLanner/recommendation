package me.lanner.liz.paper.attack;

import me.lanner.liz.paper.math.Matrix;

public interface AttackDataGenerator {

    default void analyze(Matrix sourceData) { }

    String strategy();

    Matrix generate(Matrix sourceData, Integer scalePercent);

}
