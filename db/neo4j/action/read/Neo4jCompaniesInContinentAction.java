package grakn.simulation.db.neo4j.action.read;

import grakn.simulation.db.common.action.read.CompaniesInContinentAction;
import grakn.simulation.db.common.world.World;
import grakn.simulation.db.neo4j.driver.Neo4jOperation;
import org.neo4j.driver.Query;

import java.util.HashMap;
import java.util.List;

public class Neo4jCompaniesInContinentAction extends CompaniesInContinentAction<Neo4jOperation> {
    public Neo4jCompaniesInContinentAction(Neo4jOperation dbOperation, World.Continent continent) {
        super(dbOperation, continent);
    }

    @Override
    public List<Long> run() {
        String template = "" +
                "MATCH (company:Company)-[:INCORPORATED_IN]->(country:Country)-[:LOCATED_IN]->(continent:Continent {locationName: $continentName})\n" +
                "RETURN company.companyNumber";

        HashMap<String, Object> parameters = new HashMap<String, Object>(){{
            put("continentName", continent.name());
        }};
        return dbOperation.getOrderedAttribute(new Query(template, parameters), "company.companyNumber", null);
    }
}
