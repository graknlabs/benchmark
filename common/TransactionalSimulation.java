package grakn.simulation.common;

import grakn.simulation.config.Config;
import grakn.simulation.common.driver.TransactionalDbDriver;
import grakn.simulation.common.driver.TransactionalDbOperation;
import grakn.simulation.common.world.World;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class TransactionalSimulation<DB_DRIVER extends TransactionalDbDriver<?, ?, DB_OPERATION>, DB_OPERATION extends TransactionalDbOperation> extends Simulation<DB_DRIVER, DB_OPERATION> {

    public TransactionalSimulation(DB_DRIVER driver, Map<String, Path> initialisationDataPaths, int randomSeed, World world, List<Config.Agent> agentConfigs, Function<Integer, Boolean> iterationSamplingFunction, boolean test) {
        super(driver, initialisationDataPaths, randomSeed, world, agentConfigs, iterationSamplingFunction, test);
    }

    @Override
    protected void closeIteration() {
        driver.closeSessions();
    }

    @Override
    public void close() {
        driver.close();
    }
}