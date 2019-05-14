package me.lanner.liz.paper.attack;

import me.lanner.liz.paper.math.Matrix;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Profile("rand")
public class RandomAttackDataGenerator extends AbstractAttackDataGenerator {

    private List<Integer> movieIds;
    private Random random = new Random(System.currentTimeMillis());

    @Override
    public String strategy() {
        return "rand";
    }

    @Override
    public void analyze(Matrix sourceData) {
        super.analyze(sourceData);
        Set<Integer> movieIdSet = new HashSet<>();
        for (Integer userId : sourceData.allX()) {
            for (Integer movieId : sourceData.get(userId).allX()) {
                movieIdSet.add(movieId);
            }
        }
        movieIds = new ArrayList<>(movieIdSet);
    }

    @Override
    protected Collection<Integer> movies() {
        Set<Integer> selectedMovieIds = new HashSet<>();
        while (selectedMovieIds.size() < attackRatingCount) {
            selectedMovieIds.add(movieIds.get(random.nextInt(movieIds.size())));
        }
        return selectedMovieIds;
    }

    @Override
    protected Double rate(Integer movieId) {
        return (double) random.nextInt(5);
    }
}
