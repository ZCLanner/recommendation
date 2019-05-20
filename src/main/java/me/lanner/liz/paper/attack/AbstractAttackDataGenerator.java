package me.lanner.liz.paper.attack;

import me.lanner.liz.paper.math.Matrix;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;

public abstract class AbstractAttackDataGenerator implements AttackDataGenerator {

    @Value("${attack.target-item}")
    protected int targetItem;
    @Value("${attack.rate.num}")
    protected int attackRatingCount;

    private Integer maxUserId;

    @Override
    public void analyze(Matrix<Integer> sourceData) {
        maxUserId = sourceData.allX().stream().reduce((integer, integer2) -> integer>integer2? integer : integer2).get();
    }

    @Override
    public Matrix<Integer> generate(Matrix<Integer> sourceData, Integer scalePercent) {
        Integer userCount = sourceData.length();
        userCount = userCount*scalePercent/100;
        Matrix<Integer> matrix = new Matrix<>(userCount, attackRatingCount);
        for (int i = maxUserId+1; i <= maxUserId+userCount; i++) {
            matrix.set(i, targetItem, 5.0D);
            for (Integer mId : movies()) {
                matrix.set(i, mId, rate(mId));
            }
        }
        return matrix;
    }

    protected abstract Collection<Integer> movies();

    protected abstract Double rate(Integer movieId);

}
