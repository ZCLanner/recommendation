package me.lanner.liz.paper.attack;

import me.lanner.liz.paper.math.Matrix;
import me.lanner.liz.paper.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

@Service
@Profile("attack")
public class AttackDataCsvFileStorage implements AttackDataStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttackDataCsvFileStorage.class);

    @Value("${attack.data.target}")
    private String targetFileNameTpl;

    @Override
    public void save(String strategy, int scalePercent, Matrix<Integer> matrix) throws IOException {
        String targetFileName = targetFileNameTpl.replace("{strategy}", strategy).replace("{percent}", String.valueOf(scalePercent));
        LOGGER.info("Save attack data to {}", targetFileName);
        File file = new File(targetFileName);
        File parentPath = file.getParentFile();
        if (parentPath != null && ! parentPath.exists()) {
            parentPath.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            for (Integer userId : matrix.allX()) {
                Vector<Integer, Double> userRatings = matrix.get(userId);
                for (Integer movieId : userRatings.allX()) {
                    Double rating = userRatings.get(movieId);
                    outputStream.write(String.format("%d,%d,%.0f\r\n", userId, movieId, rating).getBytes(StandardCharsets.UTF_8));
                }
            }
            outputStream.flush();
        }
    }
}
