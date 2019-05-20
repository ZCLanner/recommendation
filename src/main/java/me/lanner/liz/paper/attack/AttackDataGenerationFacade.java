package me.lanner.liz.paper.attack;

import me.lanner.liz.paper.math.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Profile("attack")
public class AttackDataGenerationFacade implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttackDataGenerationFacade.class);

    @Value("${attack.scale.percents}")
    private int[] scalePercents;

    @Autowired
    private SourceDataLoader sourceDataLoader;
    @Autowired
    private List<AttackDataGenerator> attackDataGenerators;
    @Autowired
    private AttackDataStorage attackDataStorage;

    @Override
    public void run(String... args) throws Exception {
        final Matrix sourceData = sourceDataLoader.loadSourceData();
        attackDataGenerators.forEach(g -> {
            g.analyze(sourceData);
            for (final int scalePercent :scalePercents) {
                Matrix attackData = g.generate(sourceData, scalePercent);
                try {
                    attackDataStorage.save(g.strategy(), scalePercent, attackData);
                } catch (IOException ex) {
                    LOGGER.error("Fail to save attack-data|ScalePercent={}|Generator={}|ErrMsg={}",
                            scalePercent, g, ex.getMessage(), ex);
                }
            }
        });
    }
}
