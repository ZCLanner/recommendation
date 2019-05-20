package me.lanner.liz.paper.recommend;

import me.lanner.liz.paper.math.Matrix;

import java.util.Set;

public interface Recommender<U> {

    Set<Integer> recommend(Matrix<U> matrix, Set<U> similarUsers);

}
