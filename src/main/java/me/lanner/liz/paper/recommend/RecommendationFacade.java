package me.lanner.liz.paper.recommend;

import me.lanner.liz.paper.math.Matrix;
import me.lanner.liz.paper.math.Vector;
import me.lanner.liz.paper.utils.FileUtils;
import me.lanner.liz.paper.utils.SortUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Profile("rcmd")
public class RecommendationFacade implements CommandLineRunner {

    private List<String> sourceFilePathList;
    @Value("${rcmd.similar.user.count}")
    private Integer similarUserCount;

    @Autowired
    private SourceDataLoader sourceDataLoader;
    @Autowired
    private List<VectorSimilarityCalculator> calculatorList;
    @Autowired
    private Recommender<User> recommender;

    @Override
    public void run(String... args) throws Exception {
        for (String sourceFilePath : sourceFilePathList) {
            Matrix<User> matrix = sourceDataLoader.loadSourceData(sourceFilePath);
            for (VectorSimilarityCalculator calculator : calculatorList) {
                String targetFileName = StringUtils.substringBeforeLast(sourceFilePath, "\\.") + "-" + calculator.name() + ".txt";
                File file = FileUtils.openNewFile(targetFileName);
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    for (User user : matrix.allX()) {
                        final Vector<Integer, Double> va = matrix.get(user);
                        Map<User, Double> similarityMap = new HashMap<>(matrix.length());
                        for (User ub : matrix.allX()) {
                            if (user.equals(ub) || ! user.getCategoryId().equals(ub.getCategoryId())) {
                                continue;
                            }
                            Vector<Integer, Double> vb = matrix.get(ub);
                            Double similarity = calculator.calcSimilarity(va, vb);
                            similarityMap.put(ub, similarity);
                        }
                        List<Map.Entry<User, Double>> entries = SortUtils.sortByValueDesc(similarityMap);
                        Set<User> similarUsers = new HashSet<>(similarUserCount);
                        for (int i = 0; i < similarUserCount && i < entries.size(); i++) {
                            similarUsers.add(entries.get(i).getKey());
                        }
                        Set<Integer> recommendedMovieIds = recommender.recommend(matrix, similarUsers);
                        outputStream.write(String.format("%d: %s\r\n", user.getUserId(), StringUtils.join(recommendedMovieIds, ",")).getBytes(StandardCharsets.UTF_8));
                    }
                }
            }
        }
    }
}
