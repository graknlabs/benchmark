package grakn.simulation.agents;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * Helpers for generating values based on a given random generator
 */
public class RandomValueGenerator {
    private final Random random;

    public RandomValueGenerator(Random random) {
        this.random = random;
    }

    private static double doubleInterpolate(double in, double min, double max) {
        return (in * (max - min)) + min;
    }

    int boundRandomInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    String boundRandomLengthRandomString(int minLength, int maxLength) {
        return RandomStringUtils.random(boundRandomInt(minLength, maxLength), 0, 0, true, true, null, random);
    }

    double boundRandomDouble(Double min, Double max) {
        return doubleInterpolate(random.nextDouble(), min, max);
    }

    boolean bool() {
        return random.nextBoolean();
    }
}
