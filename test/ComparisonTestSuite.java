package grakn.simulation.test;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static grakn.simulation.test.SimulationsForComparison.grakn;
import static grakn.simulation.test.SimulationsForComparison.neo4j;

public class ComparisonTestSuite extends Suite {
    private static final List<Runner> NO_RUNNERS = Collections.emptyList();
    private final List<Runner> runners;
    private final Class<?> klass;
    private static int iteration = 1;

    public ComparisonTestSuite(Class<?> klass) throws Throwable {
        super(klass, NO_RUNNERS);
        this.klass = klass;
        this.runners = Collections.unmodifiableList(createRunnersForIterations());
    }

    private List<Runner> createRunnersForIterations() {
        List<Runner> runners = new ArrayList<>();
        for (int i = 1; i <= SimulationsForComparison.numIterations; i++) {
            try {
                BlockJUnit4ClassRunner runner = new ComparisonTestRunner(klass, i);
                runners.add(runner);
            } catch (InitializationError initializationError) {
                initializationError.printStackTrace();
            }
        }
        return runners;
    }

    protected void runChild(Runner runner, final RunNotifier notifier) {
        iteration++;
        neo4j.iterate();
        grakn.iterate();
        super.runChild(runner, notifier);
        if (iteration == SimulationsForComparison.numIterations + 1) {
            grakn.close();
            neo4j.close();
        }
    }

    protected List<Runner> getChildren() {
        return this.runners;
    }
}
