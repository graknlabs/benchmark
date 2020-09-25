package grakn.simulation.db.neo4j.agents.interaction;

import grakn.simulation.db.common.agents.base.AgentResult;
import grakn.simulation.db.common.agents.interaction.PersonBirthAgentBase;
import grakn.simulation.db.common.world.World;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.getOnlyElement;
import static grakn.simulation.db.neo4j.schema.Schema.EMAIL;
import static grakn.simulation.db.neo4j.schema.Schema.DATE_OF_BIRTH;
import static grakn.simulation.db.neo4j.schema.Schema.GENDER;
import static grakn.simulation.db.neo4j.schema.Schema.FORENAME;
import static grakn.simulation.db.neo4j.schema.Schema.LOCATION_NAME;
import static grakn.simulation.db.neo4j.schema.Schema.SURNAME;
import static grakn.simulation.db.neo4j.schema.Schema.IS_CURRENT;

public class PersonBirthAgent extends Neo4jAgent<World.City> implements PersonBirthAgentBase {

    @Override
    public AgentResult insertPerson(World.City city, LocalDateTime today, String email, String gender, String forename, String surname) {
        String template = "MATCH (c:City {locationName: $locationName})" +
                "CREATE (person:Person {" +
                "email: $email, " +
                "dateOfBirth: $dateOfBirth, " +
                "gender: $gender, " +
                "forename: $forename, " +
                "surname: $surname" +
                "})-[:BORN_IN]->(c)," +
                "(person)-[:RESIDENT_OF {startDate: $dateOfBirth, isCurrent: $isCurrent}]->(c)" +
                "RETURN person.email, person.dateOfBirth, person.gender, person.forename, person.surname";

        HashMap<String, Object> parameters = new HashMap<String, Object>(){{
                put(LOCATION_NAME, city.name());
                put(EMAIL, email);
                put(DATE_OF_BIRTH, today);
                put(GENDER, gender);
                put(FORENAME, forename);
                put(SURNAME, surname);
                put(IS_CURRENT, true);
        }};
        return single_result(tx().execute(new Query(template, parameters)));
//        TODO Key constraints are possible with Neo4j Enterprise, and some constraints are supported in Community
//        https://neo4j.com/developer/kb/how-to-implement-a-primary-key-property-for-a-label/
    }

    @Override
    public AgentResult resultsForTesting(Record answer) {
        return new AgentResult() {{
            put(PersonBirthAgentField.EMAIL, answer.asMap().get("person." + EMAIL));
            put(PersonBirthAgentField.DATE_OF_BIRTH, answer.asMap().get("person." + DATE_OF_BIRTH));
            put(PersonBirthAgentField.GENDER, answer.asMap().get("person." + GENDER));
            put(PersonBirthAgentField.FORENAME, answer.asMap().get("person." + FORENAME));
            put(PersonBirthAgentField.SURNAME, answer.asMap().get("person." + SURNAME));
        }};
    }

//    protected int checkCount() {
//        String template = "" +
//                "MATCH " +
//                "(p:Person)-[:BORN_IN]->(c:City {locationName: $locationName})" +
//                "RETURN count(p), count(p.email), count(p.dateOfBirth), count(p.gender), count(p.forename), count(p.surname)";
//
//        HashMap<String, Object> parameters = new HashMap<String, Object>(){{
//            put(LOCATION_NAME, city().toString());
//        }};
//
//        Query countQuery = new Query(template, parameters);
//
//        log().query("checkCount", countQuery);
//        return tx().count(countQuery);
//    }
}
