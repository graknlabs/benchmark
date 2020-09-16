package grakn.simulation.db.neo4j.agents.interaction;

import grakn.simulation.db.common.agents.interaction.FriendshipAgentBase;
import grakn.simulation.db.common.world.World;
import grakn.simulation.db.neo4j.driver.Transaction;
import org.neo4j.driver.Query;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static grakn.simulation.db.neo4j.agents.interaction.RelocationAgent.cityResidentsQuery;

public class FriendshipAgent extends Neo4jAgent<World.City> implements FriendshipAgentBase {

    @Override
    public List<String> getResidentEmails(World.City city, LocalDateTime earliestDate) {
        Query cityResidentsQuery = cityResidentsQuery(city, earliestDate);
        log().query("getResidentEmails", cityResidentsQuery);
        return tx().getOrderedAttribute(cityResidentsQuery, "resident.email", null);
    }

    @Override
    public void insertFriendship(LocalDateTime today, String friend1Email, String friend2Email) {
        String template = "" +
                "MATCH " +
                "(p1:Person {p1Email:$p1Email}),\n" +
                "(p2:Person {p2Email:$p2Email})\n" +
                "WHERE NOT (p1)-[:FRIEND_OF]-(p2)\n" +
                "CREATE (p1)-[:FRIEND_OF {startDate: $startDate}]->(p2)"; // TODO this will surely insert duplicates

        HashMap<String, Object> parameters = new HashMap<String, Object>(){{
                put("p1Email", friend1Email);
                put("p2Email", friend2Email);
                put("startDate", today);
        }};
        Query insertFriendshipQuery = new Query(template, parameters);
        log().query("insertFriendship", insertFriendshipQuery);
        tx().execute(insertFriendshipQuery);
    }
}
