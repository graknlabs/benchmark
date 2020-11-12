/*
 * Copyright (C) 2020 Grakn Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package grakn.simulation.neo4j.action.read;

import grakn.simulation.common.action.SpouseType;
import grakn.simulation.common.action.read.MarriedCoupleAction;
import grakn.simulation.common.world.World;
import grakn.simulation.neo4j.driver.Neo4jOperation;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Neo4jMarriedCoupleAction extends MarriedCoupleAction<Neo4jOperation> {
    public Neo4jMarriedCoupleAction(Neo4jOperation dbOperation, World.City city, LocalDateTime today) {
        super(dbOperation, city, today);
    }

    @Override
    public List<HashMap<SpouseType, String>> run() {
        String template = query();
        HashMap<String, Object> parameters = new HashMap<String, Object>(){{
            put("locationName", city.name());
        }};
        List<Record> records = dbOperation.execute(new Query(template, parameters));
        return records.stream().map(Record::asMap).map(r -> new HashMap<SpouseType, String>() {{
            put(SpouseType.WIFE, r.get("wife.email").toString());
            put(SpouseType.HUSBAND, r.get("husband.email").toString());
        }}).collect(toList());
    }

    public static String query() {
        return "MATCH (city:City {locationName: $locationName}),\n" +
                "(husband:Person)-[marriage:MARRIED_TO {locationName: city.locationName}]->(wife:Person)\n" +
                "WHERE NOT (wife)<-[:CHILD_OF]-(:Person)-[:CHILD_OF]->(husband)\n" +
                "RETURN husband.email, wife.email\n" +
                "ORDER BY marriage.id ASC\n";
    }
}
