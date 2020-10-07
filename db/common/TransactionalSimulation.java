package grakn.simulation.db.common;

import grakn.simulation.config.Config;
import grakn.simulation.db.common.driver.TransactionalDbDriver;
import grakn.simulation.db.common.driver.TransactionalDbOperation;
import grakn.simulation.db.common.world.World;
import grakn.simulation.utils.RandomSource;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class TransactionalSimulation<DB_DRIVER extends TransactionalDbDriver<TRANSACTION, SESSION, DB_OPERATION>, DB_OPERATION extends TransactionalDbOperation, TRANSACTION, SESSION> extends Simulation<DB_DRIVER, DB_OPERATION> {

    public TransactionalSimulation(DB_DRIVER driver, Map<String, Path> initialisationDataPaths, RandomSource randomSource, World world, List<Config.Agent> agentConfigs, Function<Integer, Boolean> iterationSamplingFunction, boolean test) {
        super(driver, initialisationDataPaths, randomSource, world, agentConfigs, iterationSamplingFunction, test);
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
