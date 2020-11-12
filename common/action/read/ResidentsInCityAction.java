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

package grakn.simulation.common.action.read;

import grakn.simulation.common.driver.DbOperation;
import grakn.simulation.common.world.World;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class ResidentsInCityAction<DB_OPERATION extends DbOperation> extends ReadAction<DB_OPERATION, List<String>> {

    protected final World.City city;
    protected final int numResidents;
    protected final LocalDateTime earliestDate;

    public ResidentsInCityAction(DB_OPERATION dbOperation, World.City city, int numResidents, LocalDateTime earliestDate) {
        super(dbOperation);
        this.city = city;
        this.numResidents = numResidents;
        this.earliestDate = earliestDate;
    }

    @Override
    protected ArrayList<Object> inputForReport() {
        return argsList(city, numResidents, earliestDate);
    }
}
