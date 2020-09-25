package grakn.simulation.db.common.agents.interaction;

import grabl.tracing.client.GrablTracingThreadStatic.ThreadTrace;
import grakn.simulation.db.common.agents.base.Agent;
import grakn.simulation.db.common.agents.base.AgentResult;
import grakn.simulation.db.common.agents.base.AgentResultSet;
import grakn.simulation.db.common.agents.base.SimulationContext;
import grakn.simulation.db.common.world.World;

import java.time.LocalDateTime;
import java.util.List;

import static grabl.tracing.client.GrablTracingThreadStatic.traceOnThread;
import static java.util.Collections.shuffle;

public interface FriendshipAgentBase extends InteractionAgent<World.City> {

    enum FriendshipField implements Agent.ComparableField {
        FRIEND1_EMAIL, FRIEND2_EMAIL, START_DATE
    }

    @Override
    default AgentResultSet iterate(Agent<World.City, ?> agent, World.City city, SimulationContext simulationContext) {
        List<String> residentEmails;
        AgentResultSet agentResultSet = new AgentResultSet();
        agent.newAction("getResidentEmails");
        try (ThreadTrace trace = traceOnThread(agent.action())) {
            residentEmails = getResidentEmails(city, simulationContext.today());
        }
        agent.closeAction();  // TODO Closing and reopening the transaction here is a workaround for https://github.com/graknlabs/grakn/issues/5585
        agent.newAction("insertFriendship");
        if (residentEmails.size() > 0) {
            shuffle(residentEmails, agent.random());
            int numFriendships = simulationContext.world().getScaleFactor();
            for (int i = 0; i < numFriendships; i++) {
                try (ThreadTrace trace = traceOnThread(agent.action())) {
                    agentResultSet.add(insertFriendship(simulationContext.today(), agent.pickOne(residentEmails), agent.pickOne(residentEmails)));
                }
            }
            agent.commitAction();
        }
        return agentResultSet;
    }

    List<String> getResidentEmails(World.City city, LocalDateTime earliestDate);

    AgentResult insertFriendship(LocalDateTime today, String friend1Email, String friend2Email);
}
