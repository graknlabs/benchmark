package grakn.simulation.db.neo4j.action.write;

import grakn.simulation.db.common.action.Action;
import grakn.simulation.db.common.action.write.InsertFriendshipAction;
import grakn.simulation.db.neo4j.driver.Neo4jOperation;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;

import java.time.LocalDateTime;
import java.util.HashMap;

import static grakn.simulation.db.neo4j.schema.Schema.EMAIL;
import static grakn.simulation.db.neo4j.schema.Schema.START_DATE;

public class Neo4jInsertFriendshipAction extends InsertFriendshipAction<Neo4jOperation, Record> {

    public Neo4jInsertFriendshipAction(Neo4jOperation dbOperation, LocalDateTime today, String friend1Email, String friend2Email) {
        super(dbOperation, today, friend1Email, friend2Email);
    }

    @Override
    public Record run() {
        String template = "" +
                "MATCH " +
                "(p1:Person),\n" +
                "(p2:Person)\n" +
                "WHERE p1.email = $p1Email AND p2.email = $p2Email AND NOT (p1)-[:FRIEND_OF]-(p2)\n" +
                "CREATE (p1)-[friendOf:FRIEND_OF {startDate: $startDate}]->(p2)\n" +
                "RETURN p1.email, p2.email, friendOf.startDate";

        HashMap<String, Object> parameters = new HashMap<String, Object>(){{
            put("p1Email", friend1Email);
            put("p2Email", friend2Email);
            put("startDate", today);
        }};
        return Action.optionalSingleResult(dbOperation.execute(new Query(template, parameters)));
    }

    @Override
    protected HashMap<ComparableField, Object> outputForReport(Record answer) {
        return new HashMap<ComparableField, Object>() {
            {
                put(InsertFriendshipActionField.FRIEND1_EMAIL, answer.asMap().get("p1." + EMAIL));
                put(InsertFriendshipActionField.FRIEND2_EMAIL, answer.asMap().get("p2." + EMAIL));
                put(InsertFriendshipActionField.START_DATE, answer.asMap().get("friendOf." + START_DATE));
            }
        };
    }
}
