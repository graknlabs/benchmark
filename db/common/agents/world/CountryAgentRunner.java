package grakn.simulation.db.common.agents.world;

import grakn.simulation.db.common.agents.base.Agent;
import grakn.simulation.db.common.agents.base.IterationContext;
import grakn.simulation.db.common.agents.base.AgentRunner;
import grakn.simulation.utils.RandomSource;
import grakn.simulation.db.common.agents.utils.Tracker;
import grakn.simulation.db.common.world.World;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CountryAgentRunner extends AgentRunner<World.Country> {

    private final SessionStrategy sessionStrategy;

    public enum SessionStrategy {
        COUNTRY, CONTINENT
    }

    public CountryAgentRunner(Class<? extends Agent<World.Country>> agentClass, SessionStrategy sessionStrategy) {
        super(agentClass);
        this.sessionStrategy = sessionStrategy;
    }

    @Override
    protected List<World.Country> getParallelItems(IterationContext iterationContext, RandomSource randomSource) {
        return iterationContext.getWorld().getCountries().collect(toList());
    }

    @Override
    protected String getSessionKey(IterationContext iterationContext, RandomSource randomSource, World.Country country) {
        switch (sessionStrategy) {
            case COUNTRY:
                return country.name();
            case CONTINENT:
                return country.continent().name();
            default:
                throw new IllegalArgumentException("Unexpected session strategy: " + sessionStrategy.name());
        }
    }

    @Override
    protected String getTracker(IterationContext iterationContext, RandomSource randomSource, World.Country country) {
        return Tracker.of(country.continent(), country);
    }
}