package grakn.simulation.db.common.agents.interaction;

import grabl.tracing.client.GrablTracingThreadStatic;
import grakn.simulation.db.common.agents.base.Agent;
import grakn.simulation.db.common.agents.base.AgentResultSet;
import grakn.simulation.db.common.agents.base.IterationContext;
import grakn.simulation.db.common.world.Region;

import java.util.Random;

import static grabl.tracing.client.GrablTracingThreadStatic.traceOnThread;

public interface InteractionAgent<REGION extends Region> extends AutoCloseable {

    default String name() {
        return this.getClass().getSimpleName();
    }

    AgentResultSet iterate(Agent<REGION, ?> agent, REGION region, IterationContext iterationContext);

    default AgentResultSet iterateWithTracing(Agent<REGION, ?> agent, REGION region, IterationContext iterationContext, Random random) {
        try (GrablTracingThreadStatic.ThreadTrace trace = traceOnThread(name())) {
            System.out.println(name());
            return iterate(agent, region, iterationContext);
        }
    }
}
