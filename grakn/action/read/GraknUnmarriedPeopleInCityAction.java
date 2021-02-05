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

package grakn.benchmark.grakn.action.read;

import grakn.benchmark.common.action.read.UnmarriedPeopleInCityAction;
import grakn.benchmark.common.world.World;
import grakn.benchmark.grakn.driver.GraknOperation;
import graql.lang.Graql;
import graql.lang.pattern.variable.UnboundVariable;
import graql.lang.query.GraqlMatch;

import java.time.LocalDateTime;
import java.util.List;

import static grakn.benchmark.grakn.action.Model.CITY;
import static grakn.benchmark.grakn.action.Model.DATE_OF_BIRTH;
import static grakn.benchmark.grakn.action.Model.EMAIL;
import static grakn.benchmark.grakn.action.Model.END_DATE;
import static grakn.benchmark.grakn.action.Model.GENDER;
import static grakn.benchmark.grakn.action.Model.LOCATION_NAME;
import static grakn.benchmark.grakn.action.Model.MARRIAGE;
import static grakn.benchmark.grakn.action.Model.MARRIAGE_HUSBAND;
import static grakn.benchmark.grakn.action.Model.MARRIAGE_WIFE;
import static grakn.benchmark.grakn.action.Model.PERSON;
import static grakn.benchmark.grakn.action.Model.RESIDENCY;
import static grakn.benchmark.grakn.action.Model.RESIDENCY_LOCATION;
import static grakn.benchmark.grakn.action.Model.RESIDENCY_RESIDENT;
import static graql.lang.Graql.match;
import static graql.lang.Graql.not;
import static graql.lang.Graql.var;

public class GraknUnmarriedPeopleInCityAction extends UnmarriedPeopleInCityAction<GraknOperation> {
    public GraknUnmarriedPeopleInCityAction(GraknOperation dbOperation, World.City city, String gender, LocalDateTime dobOfAdults) {
        super(dbOperation, city, gender, dobOfAdults);
    }

    @Override
    public List<String> run() {

        String marriageRole;
        if (gender.equals("female")) {
            marriageRole = MARRIAGE_WIFE;
        } else if (gender.equals("male")) {
            marriageRole = MARRIAGE_HUSBAND;
        } else {
            throw new IllegalArgumentException("Gender must be male or female");
        }
        GraqlMatch query = query(marriageRole, gender, dobOfAdults, city.name());
        return dbOperation.sortedExecute(query, EMAIL, null);
    }

    public static GraqlMatch query(String marriageRole, String gender, LocalDateTime dobOfAdults, String cityName) {
        UnboundVariable personVar = var(PERSON);
        UnboundVariable cityVar = var(CITY);
        return match(
                personVar.isa(PERSON).has(GENDER, gender).has(EMAIL, var(EMAIL)).has(DATE_OF_BIRTH, var(DATE_OF_BIRTH)),
                var(DATE_OF_BIRTH).lte(dobOfAdults),
                not(var("m").rel(marriageRole, personVar).isa(MARRIAGE)),
                var("r").rel(RESIDENCY_RESIDENT, personVar).rel(RESIDENCY_LOCATION, cityVar).isa(RESIDENCY),
                not(var("r").has(END_DATE, var(END_DATE))),
                cityVar.isa(CITY).has(LOCATION_NAME, cityName)
        ).get(EMAIL);
    }
}
