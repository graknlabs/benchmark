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

package grakn.simulation.grakn.action.write;

import grakn.client.answer.ConceptMap;
import grakn.simulation.common.action.write.InsertProductAction;
import grakn.simulation.common.world.World;
import grakn.simulation.grakn.driver.GraknOperation;
import graql.lang.Graql;
import graql.lang.query.GraqlInsert;

import java.util.HashMap;

import static grakn.simulation.grakn.action.Model.CONTINENT;
import static grakn.simulation.grakn.action.Model.LOCATION_NAME;
import static grakn.simulation.grakn.action.Model.PRODUCED_IN;
import static grakn.simulation.grakn.action.Model.PRODUCED_IN_CONTINENT;
import static grakn.simulation.grakn.action.Model.PRODUCED_IN_PRODUCT;
import static grakn.simulation.grakn.action.Model.PRODUCT;
import static grakn.simulation.grakn.action.Model.PRODUCT_BARCODE;
import static grakn.simulation.grakn.action.Model.PRODUCT_DESCRIPTION;
import static grakn.simulation.grakn.action.Model.PRODUCT_NAME;

public class GraknInsertProductAction extends InsertProductAction<GraknOperation, ConceptMap> {
    public GraknInsertProductAction(GraknOperation dbOperation, World.Continent continent, Double barcode, String productName, String productDescription) {
        super(dbOperation, continent, barcode, productName, productDescription);
    }

    @Override
    public ConceptMap run() {
        GraqlInsert insertProductQuery = query(continent.name(), barcode, productName, productDescription);
        return singleResult(dbOperation.execute(insertProductQuery));
    }

    public static GraqlInsert query(String continentName, Double barcode, String productName, String productDescription) {
        return Graql.match(
                    Graql.var(CONTINENT)
                            .isa(CONTINENT)
                            .has(LOCATION_NAME, continentName)
            ).insert(
                    Graql.var(PRODUCT)
                            .isa(PRODUCT)
                            .has(PRODUCT_BARCODE, barcode)
                            .has(PRODUCT_NAME, productName)
                            .has(PRODUCT_DESCRIPTION, productDescription),
                    Graql.var(PRODUCED_IN)
                            .isa(PRODUCED_IN)
                            .rel(PRODUCED_IN_PRODUCT, Graql.var(PRODUCT))
                            .rel(PRODUCED_IN_CONTINENT, Graql.var(CONTINENT))
            );
    }

    @Override
    protected HashMap<ComparableField, Object> outputForReport(ConceptMap answer) {
        return new HashMap<ComparableField, Object>() {{
            put(InsertProductActionField.PRODUCT_BARCODE, dbOperation.getOnlyAttributeOfThing(answer, PRODUCT, PRODUCT_BARCODE));
            put(InsertProductActionField.PRODUCT_NAME, dbOperation.getOnlyAttributeOfThing(answer, PRODUCT, PRODUCT_NAME));
            put(InsertProductActionField.PRODUCT_DESCRIPTION, dbOperation.getOnlyAttributeOfThing(answer, PRODUCT, PRODUCT_DESCRIPTION));
            put(InsertProductActionField.CONTINENT, dbOperation.getOnlyAttributeOfThing(answer, CONTINENT, LOCATION_NAME));
        }};
    }
}
