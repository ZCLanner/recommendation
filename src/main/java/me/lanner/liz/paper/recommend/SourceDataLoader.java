package me.lanner.liz.paper.recommend;

import me.lanner.liz.paper.math.Matrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
@Profile("rcmd")
public class SourceDataLoader {

    @Value("${rcmd.data.source.user.count}")
    private int userCount;
    @Value("${rcmd.data.source.movie.count}")
    private int movieCount;
    @Value("${rcmd.data.source.column.user}")
    private int userColumnIndex;
    @Value("${rcmd.data.source.column.movie}")
    private int movieColumnIndex;
    @Value("${rcmd.data.source.column.rate}")
    private int rateColumnIndex;
    @Value("${rcmd.data.source.column.category}")
    private int categoryColumnIndex;
    @Value("${rcmd.data.source.column.delimiter}")
    private String columnDelimiter;

    public Matrix<User> loadSourceData(String rateFilePath) throws IOException {
        Matrix<User> matrix = new Matrix<>(userCount, movieCount);
        File rateFile = new File(rateFilePath);
        BufferedReader reader = new BufferedReader(new FileReader(rateFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] components = line.split(columnDelimiter);
            Integer userId = Integer.valueOf(components[userColumnIndex]);
            Integer categoryId = Integer.valueOf(components[categoryColumnIndex]);
            Integer movieId = Integer.valueOf(components[movieColumnIndex]);
            Double rate = Double.valueOf(components[rateColumnIndex]);
            matrix.set(new User(userId, categoryId), movieId, rate);
        }
        return matrix;
    }

}
