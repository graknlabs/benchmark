package grakn.simulation.db.neo4j.action.insight;

import grakn.simulation.db.common.action.insight.FindCurrentResidentsAction;
import grakn.simulation.db.neo4j.driver.Neo4jOperation;
import org.neo4j.driver.Query;

import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;

public class Neo4jFindCurrentResidentsAction extends FindCurrentResidentsAction<Neo4jOperation> {
    public Neo4jFindCurrentResidentsAction(Neo4jOperation dbOperation) {
        super(dbOperation);
    }

    @Override
    public List<String> run() {
        return dbOperation.sortedExecute(new Query(query()), "person.email", null);
    }

    public static String query() {
        return "MATCH (person:Person)-[residentOf:RESIDENT_OF {isCurrent:true}]->(city:City {locationName: \"Berlin\"})\n" +
                "RETURN person.email\n";
    }
}
