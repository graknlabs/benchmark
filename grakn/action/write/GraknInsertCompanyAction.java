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

package grakn.benchmark.grakn.action.write;

import grakn.benchmark.common.action.Action;
import grakn.benchmark.common.action.write.InsertCompanyAction;
import grakn.benchmark.common.world.World;
import grakn.benchmark.grakn.driver.GraknOperation;
import grakn.client.concept.answer.ConceptMap;
import graql.lang.Graql;
import graql.lang.query.GraqlInsert;

import java.time.LocalDateTime;
import java.util.HashMap;

import static grakn.benchmark.grakn.action.Model.COMPANY;
import static grakn.benchmark.grakn.action.Model.COMPANY_NAME;
import static grakn.benchmark.grakn.action.Model.COMPANY_NUMBER;
import static grakn.benchmark.grakn.action.Model.COUNTRY;
import static grakn.benchmark.grakn.action.Model.DATE_OF_INCORPORATION;
import static grakn.benchmark.grakn.action.Model.INCORPORATION;
import static grakn.benchmark.grakn.action.Model.INCORPORATION_INCORPORATED;
import static grakn.benchmark.grakn.action.Model.INCORPORATION_INCORPORATING;
import static grakn.benchmark.grakn.action.Model.LOCATION_NAME;
import static graql.lang.Graql.var;

public class GraknInsertCompanyAction extends InsertCompanyAction<GraknOperation, ConceptMap> {

    public GraknInsertCompanyAction(GraknOperation dbOperation, World.Country country, LocalDateTime today, int companyNumber, String companyName) {
        super(dbOperation, country, today, companyNumber, companyName);
    }

    @Override
    public ConceptMap run() {
        return Action.singleResult(dbOperation.execute(query(country.name(), today, companyNumber, companyName)));
    }

    public static GraqlInsert query(String countryName, LocalDateTime today, int companyNumber, String companyName) {
        return Graql.match(
                var(COUNTRY).isa(COUNTRY)
                        .has(LOCATION_NAME, countryName))
                .insert(var(COMPANY).isa(COMPANY)
                                .has(COMPANY_NAME, companyName)
                                .has(COMPANY_NUMBER, companyNumber),
                        var(INCORPORATION)
                                .rel(INCORPORATION_INCORPORATED, var(COMPANY))
                                .rel(INCORPORATION_INCORPORATING, var(COUNTRY))
                                .isa(INCORPORATION)
                                .has(DATE_OF_INCORPORATION, today)
                );
    }

    @Override
    protected HashMap<ComparableField, Object> outputForReport(ConceptMap answer) {
        return new HashMap<ComparableField, Object>() {
            {
                put(InsertCompanyActionField.COMPANY_NAME, dbOperation.getOnlyAttributeOfThing(answer, COMPANY, COMPANY_NAME));
                put(InsertCompanyActionField.COMPANY_NUMBER, dbOperation.getOnlyAttributeOfThing(answer, COMPANY, COMPANY_NUMBER));
                put(InsertCompanyActionField.COUNTRY, dbOperation.getOnlyAttributeOfThing(answer, COUNTRY, LOCATION_NAME));
                put(InsertCompanyActionField.DATE_OF_INCORPORATION, dbOperation.getOnlyAttributeOfThing(answer, INCORPORATION, DATE_OF_INCORPORATION));
            }
        };
    }
}
