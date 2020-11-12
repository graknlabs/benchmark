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

package grakn.simulation.grakn.action.read;

import grakn.simulation.common.action.read.ProductsInContinentAction;
import grakn.simulation.common.world.World;
import grakn.simulation.grakn.driver.GraknOperation;
import graql.lang.Graql;
import graql.lang.query.GraqlGet;

import java.util.List;

import static grakn.simulation.grakn.action.Model.CONTINENT;
import static grakn.simulation.grakn.action.Model.LOCATION_NAME;
import static grakn.simulation.grakn.action.Model.PRODUCED_IN;
import static grakn.simulation.grakn.action.Model.PRODUCED_IN_CONTINENT;
import static grakn.simulation.grakn.action.Model.PRODUCED_IN_PRODUCT;
import static grakn.simulation.grakn.action.Model.PRODUCT;
import static grakn.simulation.grakn.action.Model.PRODUCT_BARCODE;

public class GraknProductsInContinentAction extends ProductsInContinentAction<GraknOperation> {

    public GraknProductsInContinentAction(GraknOperation dbOperation, World.Continent continent) {
        super(dbOperation, continent);
    }

    @Override
    public List<Double> run() {
        GraqlGet.Unfiltered query = query(continent.name());
        return dbOperation.sortedExecute(query, PRODUCT_BARCODE, null);
    }

    public static GraqlGet.Unfiltered query(String continentName) {
        return Graql.match(
                    Graql.var(CONTINENT)
                            .isa(CONTINENT)
                            .has(LOCATION_NAME, continentName),
                    Graql.var(PRODUCT)
                            .isa(PRODUCT)
                            .has(PRODUCT_BARCODE, Graql.var(PRODUCT_BARCODE)),
                    Graql.var(PRODUCED_IN)
                            .isa(PRODUCED_IN)
                            .rel(PRODUCED_IN_PRODUCT, Graql.var(PRODUCT))
                            .rel(PRODUCED_IN_CONTINENT, Graql.var(CONTINENT))

            ).get();
    }
}
