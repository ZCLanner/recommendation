package me.lanner.liz.paper.attack;

import me.lanner.liz.paper.math.Matrix;

import java.io.IOException;

public interface AttackDataStorage {

    void save(String strategy, int scalePercent, Matrix matrix) throws IOException;

}
