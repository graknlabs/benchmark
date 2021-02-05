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

package grakn.benchmark.grakn.action.insight;

import grakn.benchmark.common.action.insight.ThreeHopAction;
import grakn.benchmark.grakn.driver.GraknOperation;
import graql.lang.Graql;
import graql.lang.query.GraqlMatch;

import java.util.List;

import static grakn.benchmark.grakn.action.Model.BORN_IN;
import static grakn.benchmark.grakn.action.Model.BORN_IN_CHILD;
import static grakn.benchmark.grakn.action.Model.BORN_IN_PLACE_OF_BIRTH;
import static grakn.benchmark.grakn.action.Model.CITY;
import static grakn.benchmark.grakn.action.Model.COMPANY;
import static grakn.benchmark.grakn.action.Model.COMPANY_NAME;
import static grakn.benchmark.grakn.action.Model.EMPLOYMENT;
import static grakn.benchmark.grakn.action.Model.EMPLOYMENT_EMPLOYEE;
import static grakn.benchmark.grakn.action.Model.EMPLOYMENT_EMPLOYER;
import static grakn.benchmark.grakn.action.Model.LOCATION_NAME;
import static grakn.benchmark.grakn.action.Model.PARENTSHIP;
import static grakn.benchmark.grakn.action.Model.PARENTSHIP_CHILD;
import static grakn.benchmark.grakn.action.Model.PARENTSHIP_PARENT;
import static grakn.benchmark.grakn.action.Model.PERSON;
import static graql.lang.Graql.match;
import static graql.lang.Graql.var;

public class GraknThreeHopAction extends ThreeHopAction<GraknOperation> {
    public GraknThreeHopAction(GraknOperation dbOperation) {
        super(dbOperation);
    }

    @Override
    public List<String> run() {
        return dbOperation.sortedExecute(query(), COMPANY_NAME, null);
    }

    public static GraqlMatch.Unfiltered query() {
        return match(
                var(CITY).isa(CITY).has(LOCATION_NAME, "London"),
                var().rel(BORN_IN_PLACE_OF_BIRTH, var(CITY)).rel(BORN_IN_CHILD, var("child")).isa(BORN_IN),
                var("child").isa(PERSON),
                var().rel(PARENTSHIP_PARENT, var("parent")).rel(PARENTSHIP_CHILD, var("child")).isa(PARENTSHIP),
                var("parent").isa(PERSON),
                var().rel(EMPLOYMENT_EMPLOYEE, var("parent")).rel(EMPLOYMENT_EMPLOYER, var(COMPANY)).isa(EMPLOYMENT),
                var(COMPANY).isa(COMPANY).has(COMPANY_NAME, var(COMPANY_NAME))
        );
    }
}
