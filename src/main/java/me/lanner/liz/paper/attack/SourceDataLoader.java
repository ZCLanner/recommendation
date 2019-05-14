package me.lanner.liz.paper.attack;

import me.lanner.liz.paper.math.Matrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class SourceDataLoader {

    @Value("${attack.data.source}")
    private String rateFilePath;

    @Value("${attack.data.source.user.count}")
    private int userCount;
    @Value("${attack.data.source.movie.count}")
    private int movieCount;
    @Value("${attack.data.source.column.user}")
    private int userColumnIndex;
    @Value("${attack.data.source.column.movie}")
    private int movieColumnIndex;
    @Value("${attack.data.source.column.rate}")
    private int rateColumnIndex;
    @Value("${attack.data.source.column.delimiter}")
    private String columnDelimiter;

    public Matrix loadSourceData() throws IOException {
        Matrix matrix = new Matrix(userCount, movieCount);
        File rateFile = new File(rateFilePath);
        BufferedReader reader = new BufferedReader(new FileReader(rateFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] components = line.split(columnDelimiter);
            Integer userId = Integer.valueOf(components[userColumnIndex]);
            Integer movieId = Integer.valueOf(components[movieColumnIndex]);
            Double rate = Double.valueOf(components[rateColumnIndex]);
            matrix.set(userId, movieId, rate);
        }
        return matrix;
    }

}
