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

package grakn.simulation.common.action.write;

import grakn.simulation.common.action.Action;
import grakn.simulation.common.driver.DbOperation;
import grakn.simulation.common.world.World;

import java.util.ArrayList;

public abstract class InsertProductAction<DB_OPERATION extends DbOperation, ACTION_RETURN_TYPE> extends Action<DB_OPERATION, ACTION_RETURN_TYPE> {
    protected final World.Continent continent;
    protected final Double barcode;
    protected final String productName;
    protected final String productDescription;

    public InsertProductAction(DB_OPERATION dbOperation, World.Continent continent, Double barcode, String productName, String productDescription) {
        super(dbOperation);
        this.continent = continent;
        this.barcode = barcode;
        this.productName = productName;
        this.productDescription = productDescription;
    }

    @Override
    protected ArrayList<Object> inputForReport() {
        return argsList(continent, barcode, productName, productDescription);
    }

    public enum InsertProductActionField implements ComparableField {
        PRODUCT_BARCODE, PRODUCT_NAME, PRODUCT_DESCRIPTION, CONTINENT
    }
}
